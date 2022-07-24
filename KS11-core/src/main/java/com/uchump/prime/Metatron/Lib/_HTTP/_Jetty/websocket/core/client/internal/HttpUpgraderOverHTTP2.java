package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.internal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpUpgrader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.PreEncodedHttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.CoreClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketConstants;

public class HttpUpgraderOverHTTP2 implements HttpUpgrader
{
    private static final PreEncodedHttpField WS_VERSION_FIELD = new PreEncodedHttpField(HttpHeader.SEC_WEBSOCKET_VERSION, WebSocketConstants.SPEC_VERSION_STRING);
    private final CoreClientUpgradeRequest clientUpgradeRequest;

    public HttpUpgraderOverHTTP2(CoreClientUpgradeRequest clientUpgradeRequest)
    {
        this.clientUpgradeRequest = clientUpgradeRequest;
    }

    @Override
    public void prepare(HttpRequest request)
    {
        request.upgradeProtocol("websocket")
            .method(HttpMethod.CONNECT)
            .headers(headers -> headers.put(WS_VERSION_FIELD));

        // Notify the UpgradeListeners now the headers are set.
        clientUpgradeRequest.requestComplete();
    }

    @Override
    public void upgrade(HttpResponse response, EndPoint endPoint, Callback callback)
    {
        try
        {
            clientUpgradeRequest.upgrade(response, endPoint);
            callback.succeeded();
        }
        catch (Throwable x)
        {
            callback.failed(x);
        }
    }
}