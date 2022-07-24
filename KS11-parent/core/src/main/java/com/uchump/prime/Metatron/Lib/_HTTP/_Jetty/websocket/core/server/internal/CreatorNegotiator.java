package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.internal;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.HttpConfiguration.Customizer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.FrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketCreator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;


public class CreatorNegotiator extends WebSocketNegotiator.AbstractNegotiator
{
    private final WebSocketCreator creator;
    private final FrameHandlerFactory factory;

    public CreatorNegotiator(WebSocketCreator creator, FrameHandlerFactory factory)
    {
        this(creator, factory, null);
    }

    public CreatorNegotiator(WebSocketCreator creator, FrameHandlerFactory factory, Customizer customizer)
    {
        super(customizer);
        this.creator = creator;
        this.factory = factory;
    }

    public WebSocketCreator getWebSocketCreator()
    {
        return creator;
    }

    @Override
    public FrameHandler negotiate(WebSocketNegotiation negotiation) throws IOException
    {
        ServletContext servletContext = negotiation.getRequest().getServletContext();
        if (servletContext == null)
            throw new IllegalStateException("null servletContext from request");

        ServerUpgradeRequest upgradeRequest = new ServerUpgradeRequest(negotiation);
        ServerUpgradeResponse upgradeResponse = new ServerUpgradeResponse(negotiation);

        AtomicReference<Object> result = new AtomicReference<>();
        ((ContextHandler.Context)servletContext).getContextHandler().handle(() ->
            result.set(creator.createWebSocket(upgradeRequest, upgradeResponse)));
        Object websocketPojo = result.get();

        // Handling for response forbidden (and similar paths)
        if (upgradeResponse.isCommitted())
            return null;

        if (websocketPojo == null)
        {
            // no creation, sorry
            upgradeResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "WebSocket Endpoint Creation Refused");
            return null;
        }

        return factory.newFrameHandler(websocketPojo, upgradeRequest, upgradeResponse);
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x{%s,%s}", getClass().getSimpleName(), hashCode(), creator, factory);
    }
}