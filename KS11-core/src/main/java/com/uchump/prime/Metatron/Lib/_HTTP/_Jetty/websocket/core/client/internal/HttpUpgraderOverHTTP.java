package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.internal;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponseException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpUpgrader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpFields;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.PreEncodedHttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.CoreClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketConstants;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCore;

public class HttpUpgraderOverHTTP implements HttpUpgrader
{
    private static final PreEncodedHttpField WS_VERSION_FIELD = new PreEncodedHttpField(HttpHeader.SEC_WEBSOCKET_VERSION, WebSocketConstants.SPEC_VERSION_STRING);
    private static final PreEncodedHttpField WS_UPGRADE_FIELD = new PreEncodedHttpField(HttpHeader.UPGRADE, "websocket");
    private static final PreEncodedHttpField WS_CONNECTION_FIELD = new PreEncodedHttpField(HttpHeader.CONNECTION, "Upgrade");
    private static final PreEncodedHttpField PRAGMA_NO_CACHE_FIELD = new PreEncodedHttpField(HttpHeader.PRAGMA, "no-cache");
    private static final PreEncodedHttpField CACHE_CONTROL_NO_CACHE_FIELD = new PreEncodedHttpField(HttpHeader.CACHE_CONTROL, "no-cache");
    private final CoreClientUpgradeRequest clientUpgradeRequest;

    public HttpUpgraderOverHTTP(CoreClientUpgradeRequest clientUpgradeRequest)
    {
        this.clientUpgradeRequest = clientUpgradeRequest;
    }

    @Override
    public void prepare(HttpRequest request)
    {
        request.method(HttpMethod.GET).version(HttpVersion.HTTP_1_1)
            .headers(headers -> headers
                .put(WS_VERSION_FIELD)
                .put(WS_UPGRADE_FIELD)
                .put(WS_CONNECTION_FIELD)
                .put(HttpHeader.SEC_WEBSOCKET_KEY, generateRandomKey())
                // Per the hybi list: Add no-cache headers to avoid compatibility issue.
                // There are some proxies that rewrite "Connection: upgrade" to
                // "Connection: close" in the response if a request doesn't contain
                // these headers.
                .put(PRAGMA_NO_CACHE_FIELD)
                .put(CACHE_CONTROL_NO_CACHE_FIELD));

        // Notify the UpgradeListeners now the headers are set.
        clientUpgradeRequest.requestComplete();
    }

    private String generateRandomKey()
    {
        byte[] bytes = new byte[16];
        ThreadLocalRandom.current().nextBytes(bytes);
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.US_ASCII);
    }

    @Override
    public void upgrade(HttpResponse response, EndPoint endPoint, Callback callback)
    {
        HttpRequest request = (HttpRequest)response.getRequest();
        HttpFields requestHeaders = request.getHeaders();
        if (requestHeaders.contains(HttpHeader.UPGRADE, "websocket"))
        {
            HttpFields responseHeaders = response.getHeaders();
            if (responseHeaders.contains(HttpHeader.CONNECTION, "upgrade"))
            {
                // Check the Accept hash
                String reqKey = requestHeaders.get(HttpHeader.SEC_WEBSOCKET_KEY);
                String expectedHash = WebSocketCore.hashKey(reqKey);
                String respHash = responseHeaders.get(HttpHeader.SEC_WEBSOCKET_ACCEPT);
                if (expectedHash.equalsIgnoreCase(respHash))
                {
                    clientUpgradeRequest.upgrade(response, endPoint);
                    callback.succeeded();
                }
                else
                    callback.failed(new HttpResponseException("Invalid Sec-WebSocket-Accept hash (was: " + respHash + " expected: " + expectedHash + ")", response));
            }
            else
            {
                callback.failed(new HttpResponseException("WebSocket upgrade missing 'Connection: Upgrade' header", response));
            }
        }
        else
        {
            callback.failed(new HttpResponseException("Not a WebSocket upgrade", response));
        }
    }
}