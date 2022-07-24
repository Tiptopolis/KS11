package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.function.Function;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.DefaultHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.HandlerList;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.QueuedThreadPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketUpgradeHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.framehandlers.FrameEcho;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.framehandlers.WholeMessageEcho;

public class CoreServer extends ContainerLifeCycle
{
    private Server server;
    private ServerConnector connector;
    private WebSocketNegotiator negotiator;
    private URI serverUri;
    private URI wsUri;

    public CoreServer(Function<WebSocketNegotiation, FrameHandler> negotiationFunction)
    {
        this(new WebSocketNegotiator.AbstractNegotiator()
        {
            @Override
            public FrameHandler negotiate(WebSocketNegotiation negotiation) throws IOException
            {
                return negotiationFunction.apply(negotiation);
            }
        });
    }

    public CoreServer(WebSocketNegotiator negotiator)
    {
        this.negotiator = negotiator;
    }

    @Override
    protected void doStart() throws Exception
    {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName("qtp-CoreServer");

        server = new Server(threadPool);

        // Basic HTTP connector
        connector = new ServerConnector(server);

        // Add network connector
        server.addConnector(connector);

        // Add Handler
        WebSocketUpgradeHandler upgradeHandler = new WebSocketUpgradeHandler();
        upgradeHandler.addMapping("/*", negotiator);
        server.setHandler(new HandlerList(upgradeHandler, new DefaultHandler()));

        // Start Server
        addBean(server);

        super.doStart();

        // Establish the Server URI
        serverUri = server.getURI().resolve("/");
        wsUri = WSURI.toWebsocket(serverUri);
        super.doStart();
    }

    public URI getServerUri()
    {
        return serverUri;
    }

    public URI getWsUri()
    {
        return wsUri;
    }

    public static class EchoNegotiator extends WebSocketNegotiator.AbstractNegotiator
    {
        @Override
        public FrameHandler negotiate(WebSocketNegotiation negotiation) throws IOException
        {
            List<String> offeredSubProtocols = negotiation.getOfferedSubprotocols();

            if (offeredSubProtocols.isEmpty())
            {
                return new WholeMessageEcho();
            }
            else
            {
                for (String offeredSubProtocol : negotiation.getOfferedSubprotocols())
                {
                    if ("echo-whole".equalsIgnoreCase(offeredSubProtocol))
                    {
                        negotiation.setSubprotocol("echo-whole");
                        return new WholeMessageEcho();
                    }

                    if ("echo-frames".equalsIgnoreCase(offeredSubProtocol))
                    {
                        negotiation.setSubprotocol("echo-frames");
                        return new FrameEcho();
                    }
                }
                // no frame handler available for offered subprotocols
                return null;
            }
        }
    }
}