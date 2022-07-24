package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.RetainableByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Connector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.HttpChannel;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;

public class RFC8441Handshaker extends AbstractHandshaker
{
    @Override
    protected boolean validateRequest(HttpServletRequest request)
    {
        if (!HttpMethod.CONNECT.is(request.getMethod()))
        {
            if (LOG.isDebugEnabled())
                LOG.debug("not upgraded method!=GET {}", request);
            return false;
        }

        if (!HttpVersion.HTTP_2.is(request.getProtocol()))
        {
            if (LOG.isDebugEnabled())
                LOG.debug("not upgraded HttpVersion!=2 {}", request);
            return false;
        }

        return true;
    }

    @Override
    protected WebSocketNegotiation newNegotiation(HttpServletRequest request, HttpServletResponse response, WebSocketComponents webSocketComponents)
    {
        return new RFC8441Negotiation(Request.getBaseRequest(request), request, response, webSocketComponents);
    }

    @Override
    protected boolean validateFrameHandler(FrameHandler frameHandler, HttpServletResponse response)
    {
        if (frameHandler == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("not upgraded: no frame handler provided");

            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE_503);
        }

        return true;
    }

    @Override
    protected WebSocketConnection createWebSocketConnection(Request baseRequest, WebSocketCoreSession coreSession)
    {
        HttpChannel httpChannel = baseRequest.getHttpChannel();
        Connector connector = httpChannel.getConnector();
        EndPoint endPoint = httpChannel.getTunnellingEndPoint();
        ByteBufferPool byteBufferPool = connector.getByteBufferPool();
        RetainableByteBufferPool retainableByteBufferPool = byteBufferPool.asRetainableByteBufferPool();
        return newWebSocketConnection(endPoint, connector.getExecutor(), connector.getScheduler(), byteBufferPool, retainableByteBufferPool, coreSession);
    }

    @Override
    protected void prepareResponse(Response response, WebSocketNegotiation negotiation)
    {
        response.setStatus(HttpStatus.OK_200);
    }
}