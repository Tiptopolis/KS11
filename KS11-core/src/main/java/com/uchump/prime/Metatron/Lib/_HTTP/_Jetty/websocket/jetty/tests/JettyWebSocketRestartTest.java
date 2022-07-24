package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.FilterHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketServerComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServerContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.servlet.WebSocketUpgradeFilter;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JettyWebSocketRestartTest
{
    private Server server;
    private ServerConnector connector;
    private WebSocketClient client;
    private ServletContextHandler contextHandler;

    @BeforeEach
    public void before() throws Exception
    {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);

        client = new WebSocketClient();
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        client.stop();
        server.stop();
    }

    @Test
    public void testWebSocketRestart() throws Exception
    {
        JettyWebSocketServletContainerInitializer.configure(contextHandler, (context, container) ->
            container.addMapping("/", EchoSocket.class));
        server.start();

        int numEventListeners = contextHandler.getEventListeners().size();
        for (int i = 0; i < 100; i++)
        {
            server.stop();
            server.start();
            testEchoMessage();
        }

        // We have not accumulated websocket resources by restarting.
        assertThat(contextHandler.getEventListeners().size(), is(numEventListeners));
        assertThat(contextHandler.getContainedBeans(JettyWebSocketServerContainer.class).size(), is(1));
        assertThat(contextHandler.getContainedBeans(WebSocketServerComponents.class).size(), is(1));
        assertNotNull(contextHandler.getServletContext().getAttribute(WebSocketServerComponents.WEBSOCKET_COMPONENTS_ATTRIBUTE));
        assertNotNull(contextHandler.getServletContext().getAttribute(JettyWebSocketServerContainer.JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE));

        // We have one filter, and it is a WebSocketUpgradeFilter.
        FilterHolder[] filters = contextHandler.getServletHandler().getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getFilter(), instanceOf(WebSocketUpgradeFilter.class));

        // After stopping the websocket resources are cleaned up.
        server.stop();
        assertThat(contextHandler.getEventListeners().size(), is(0));
        assertThat(contextHandler.getContainedBeans(JettyWebSocketServerContainer.class).size(), is(0));
        assertThat(contextHandler.getContainedBeans(WebSocketServerComponents.class).size(), is(0));
        assertNull(contextHandler.getServletContext().getAttribute(WebSocketServerComponents.WEBSOCKET_COMPONENTS_ATTRIBUTE));
        assertNull(contextHandler.getServletContext().getAttribute(JettyWebSocketServerContainer.JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE));
        assertThat(contextHandler.getServletHandler().getFilters().length, is(0));
    }

    private void testEchoMessage() throws Exception
    {
        // Test we can upgrade to websocket and send a message.
        URI uri = URI.create("ws://localhost:" + connector.getLocalPort());
        EventSocket socket = new EventSocket();
        CompletableFuture<Session> connect = client.connect(socket, uri);
        try (Session session = connect.get(5, TimeUnit.SECONDS))
        {
            session.getRemote().sendString("hello world");
        }
        assertTrue(socket.closeLatch.await(10, TimeUnit.SECONDS));

        String msg = socket.textMessages.poll();
        assertThat(msg, is("hello world"));
    }
}