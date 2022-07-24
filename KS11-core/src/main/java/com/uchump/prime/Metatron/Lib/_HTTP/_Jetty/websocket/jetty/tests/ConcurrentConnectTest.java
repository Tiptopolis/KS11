package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketSessionListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.WebSocketSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcurrentConnectTest
{
    private static final int MAX_CONNECTIONS = 150;

    private Server server;
    private WebSocketClient client;
    private URI uri;

    @BeforeEach
    public void start() throws Exception
    {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(0);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        JettyWebSocketServlet servlet = new JettyWebSocketServlet()
        {
            @Override
            protected void configure(JettyWebSocketServletFactory factory)
            {
                factory.register(EchoSocket.class);
            }
        };

        context.addServlet(new ServletHolder(servlet), "/");
        server.setHandler(context);
        JettyWebSocketServletContainerInitializer.configure(context, null);

        server.start();
        uri = new URI("ws://localhost:" + connector.getLocalPort());

        client = new WebSocketClient();
        client.getHttpClient().setMaxConnectionsPerDestination(MAX_CONNECTIONS);
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        client.stop();
        server.stop();
    }

    @Test
    public void testConcurrentConnect() throws Exception
    {
        List<EventSocket> listeners = new ArrayList();
        CloseListener closeListener = new CloseListener();
        client.addSessionListener(closeListener);
        final int messages = MAX_CONNECTIONS;

        for (int i = 0; i < messages; i++)
        {
            try
            {
                EventSocket wsListener = new EventSocket();
                listeners.add(wsListener);
                client.connect(wsListener, uri);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        for (EventSocket l : listeners)
        {
            assertTrue(l.openLatch.await(5, TimeUnit.SECONDS));
        }

        for (EventSocket l : listeners)
        {
            l.session.getRemote().sendString("ping");
            assertThat(l.textMessages.poll(5, TimeUnit.SECONDS), is("ping"));
            l.session.close(StatusCode.NORMAL, "close from client");
        }

        for (EventSocket l : listeners)
        {
            assertTrue(l.closeLatch.await(5, TimeUnit.SECONDS));
            assertThat(l.closeCode, is(StatusCode.NORMAL));
            assertThat(l.closeReason, is("close from client"));
            assertNull(l.error);
        }

        closeListener.closeLatch.await(5, TimeUnit.SECONDS);
        assertTrue(client.getOpenSessions().isEmpty());
        assertTrue(client.getContainedBeans(WebSocketSession.class).isEmpty());
    }

    public static class CloseListener implements WebSocketSessionListener
    {
        public CountDownLatch closeLatch = new CountDownLatch(MAX_CONNECTIONS);

        @Override
        public void onWebSocketSessionClosed(Session session)
        {
            closeLatch.countDown();
        }
    }
}