package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.concurrent.TimeUnit;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JettyWebSocketServletAttributeTest
{
    private Server server;
    private ServerConnector connector;
    private WebSocketClient client;
    private final EchoSocket serverEndpoint = new EchoSocket();

    @BeforeEach
    public void before()
    {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        client = new WebSocketClient();
    }

    @AfterEach
    public void stop() throws Exception
    {
        client.stop();
        server.stop();
    }

    public void start(JettyWebSocketServletContainerInitializer.Configurator configurator) throws Exception
    {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);
        JettyWebSocketServletContainerInitializer.configure(contextHandler, configurator);

        server.start();
        client.start();
    }

    @Test
    public void testAttributeSetInNegotiation() throws Exception
    {
        start((context, container) -> container.addMapping("/", (req, resp) ->
        {
            req.setServletAttribute("myWebSocketCustomAttribute", "true");
            return serverEndpoint;
        }));

        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/filterPath");
        EventSocket clientEndpoint = new EventSocket();
        client.connect(clientEndpoint, uri);
        assertTrue(clientEndpoint.openLatch.await(5, TimeUnit.SECONDS));
        assertTrue(serverEndpoint.openLatch.await(5, TimeUnit.SECONDS));

        // We should have our custom attribute on the upgraded request, which was set in the negotiation.
        JettyServerUpgradeRequest upgradeRequest = (JettyServerUpgradeRequest)serverEndpoint.session.getUpgradeRequest();
        assertThat(upgradeRequest.getServletAttribute("myWebSocketCustomAttribute"), is("true"));

        clientEndpoint.session.close();
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
    }
}