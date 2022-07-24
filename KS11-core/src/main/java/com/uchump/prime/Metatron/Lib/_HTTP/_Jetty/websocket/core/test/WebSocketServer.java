package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.NetworkConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ssl.SslContextFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketUpgradeHandler;


public class WebSocketServer
{
    private final Server server = new Server();
    private URI serverUri;

    public void start() throws Exception
    {
        server.start();
        serverUri = new URI("ws://localhost:" + getLocalPort());
    }

    public void stop() throws Exception
    {
        server.stop();
    }

    public int getLocalPort()
    {
        return server.getBean(NetworkConnector.class).getLocalPort();
    }

    public Server getServer()
    {
        return server;
    }

    public WebSocketServer(FrameHandler frameHandler)
    {
        this(new DefaultNegotiator(frameHandler), false);
    }

    public WebSocketServer(WebSocketNegotiator negotiator)
    {
        this(negotiator, false);
    }

    public WebSocketServer(FrameHandler frameHandler, boolean tls)
    {
        this(new DefaultNegotiator(frameHandler), tls);
    }

    public WebSocketServer(WebSocketNegotiator negotiator, boolean tls)
    {
        this(new WebSocketComponents(), negotiator, tls);
    }

    public WebSocketServer(WebSocketComponents components, WebSocketNegotiator negotiator, boolean tls)
    {
        ServerConnector connector;
        if (tls)
            connector = new ServerConnector(server, createServerSslContextFactory());
        else
            connector = new ServerConnector(server);
        server.addConnector(connector);

        ContextHandler context = new ContextHandler("/");
        server.setHandler(context);

        WebSocketUpgradeHandler upgradeHandler = new WebSocketUpgradeHandler(components);
        upgradeHandler.addMapping("/*", negotiator);
        context.setHandler(upgradeHandler);
    }

    private SslContextFactory.Server createServerSslContextFactory()
    {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath("src/test/resources/keystore.p12");
        sslContextFactory.setKeyStorePassword("storepwd");
        return sslContextFactory;
    }

    public URI getUri()
    {
        return serverUri;
    }

    private static class DefaultNegotiator extends WebSocketNegotiator.AbstractNegotiator
    {
        private final FrameHandler frameHandler;

        public DefaultNegotiator(FrameHandler frameHandler)
        {
            this.frameHandler = frameHandler;
        }

        @Override
        public FrameHandler negotiate(WebSocketNegotiation negotiation) throws IOException
        {
            List<String> offeredSubprotocols = negotiation.getOfferedSubprotocols();
            if (!offeredSubprotocols.isEmpty())
                negotiation.setSubprotocol(offeredSubprotocols.get(0));

            return frameHandler;
        }
    }
}