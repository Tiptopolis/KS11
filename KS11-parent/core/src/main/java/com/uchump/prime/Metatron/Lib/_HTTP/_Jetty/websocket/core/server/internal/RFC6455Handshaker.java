package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.BadMessageException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpFields;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.PreEncodedHttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.RetainableByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Connector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.HttpChannel;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCore;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;

public final class RFC6455Handshaker extends AbstractHandshaker
{
    private static final HttpField UPGRADE_WEBSOCKET = new PreEncodedHttpField(HttpHeader.UPGRADE, "websocket");
    private static final HttpField CONNECTION_UPGRADE = new PreEncodedHttpField(HttpHeader.CONNECTION, HttpHeader.UPGRADE.asString());

    @Override
    protected boolean validateRequest(HttpServletRequest request)
    {
        if (!HttpMethod.GET.is(request.getMethod()))
        {
            if (LOG.isDebugEnabled())
                LOG.debug("not upgraded method!=GET {}", request);
            return false;
        }

        if (!HttpVersion.HTTP_1_1.is(request.getProtocol()))
        {
            if (LOG.isDebugEnabled())
                LOG.debug("not upgraded version!=1.1 {}", request);
            return false;
        }

        return true;
    }

    @Override
    protected WebSocketNegotiation newNegotiation(HttpServletRequest request, HttpServletResponse response, WebSocketComponents webSocketComponents)
    {
        return new RFC6455Negotiation(Request.getBaseRequest(request), request, response, webSocketComponents);
    }

    @Override
    protected boolean validateNegotiation(WebSocketNegotiation negotiation)
    {
        boolean result = super.validateNegotiation(negotiation);
        if (!result)
            return false;
        if (((RFC6455Negotiation)negotiation).getKey() == null)
            throw new BadMessageException("Missing request header 'Sec-WebSocket-Key'");
        return true;
    }

    @Override
    protected boolean validateFrameHandler(FrameHandler frameHandler, HttpServletResponse response)
    {
        if (frameHandler == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("not upgraded: no frame handler provided");
            return false;
        }

        return true;
    }

    @Override
    protected WebSocketConnection createWebSocketConnection(Request baseRequest, WebSocketCoreSession coreSession)
    {
        HttpChannel httpChannel = baseRequest.getHttpChannel();
        Connector connector = httpChannel.getConnector();
        ByteBufferPool byteBufferPool = connector.getByteBufferPool();
        RetainableByteBufferPool retainableByteBufferPool = byteBufferPool.asRetainableByteBufferPool();
        return newWebSocketConnection(httpChannel.getEndPoint(), connector.getExecutor(), connector.getScheduler(), byteBufferPool, retainableByteBufferPool, coreSession);
    }

    @Override
    protected void prepareResponse(Response response, WebSocketNegotiation negotiation)
    {
        response.setStatus(HttpServletResponse.SC_SWITCHING_PROTOCOLS);
        HttpFields.Mutable responseFields = response.getHttpFields();
        responseFields.put(UPGRADE_WEBSOCKET);
        responseFields.put(CONNECTION_UPGRADE);
        responseFields.put(HttpHeader.SEC_WEBSOCKET_ACCEPT, WebSocketCore.hashKey(((RFC6455Negotiation)negotiation).getKey()));
    }
}