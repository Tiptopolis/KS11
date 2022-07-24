package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.util.WSURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GracefulCloseTest
{
    private final EventSocket serverEndpoint = new EchoSocket();
    private Server server;
    private URI serverUri;
    private WebSocketClient client;

    @BeforeEach
    public void before() throws Exception
    {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);
        JettyWebSocketServletContainerInitializer.configure(contextHandler, (context, container) ->
            container.addMapping("/", ((req, resp) -> serverEndpoint)));
        server.start();
        serverUri = WSURI.toWebsocket(server.getURI());

        // StopTimeout is necessary for the websocket server sessions to gracefully close.
        server.setStopTimeout(1000);

        client = new WebSocketClient();
        client.setStopTimeout(1000);
        client.start();
    }

    @AfterEach
    public void after() throws Exception
    {
        client.stop();
        server.stop();
    }

    @Test
    public void testClientStop() throws Exception
    {
        EventSocket clientEndpoint = new EventSocket();
        client.connect(clientEndpoint, serverUri).get(5, TimeUnit.SECONDS);

        client.stop();

        // Check that the client endpoint was closed with the correct status code and no error.
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.closeCode, is(StatusCode.SHUTDOWN));
        assertNull(clientEndpoint.error);

        // Check that the server endpoint was closed with the correct status code and no error.
        assertTrue(serverEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(serverEndpoint.closeCode, is(StatusCode.SHUTDOWN));
        assertNull(serverEndpoint.error);
    }

    @Test
    public void testServerStop() throws Exception
    {
        EventSocket clientEndpoint = new EventSocket();
        client.connect(clientEndpoint, serverUri).get(5, TimeUnit.SECONDS);

        server.stop();

        // Check that the client endpoint was closed with the correct status code and no error.
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.closeCode, is(StatusCode.SHUTDOWN));
        assertNull(clientEndpoint.error);

        // Check that the server endpoint was closed with the correct status code and no error.
        assertTrue(serverEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(serverEndpoint.closeCode, is(StatusCode.SHUTDOWN));
        assertNull(serverEndpoint.error);
    }
}