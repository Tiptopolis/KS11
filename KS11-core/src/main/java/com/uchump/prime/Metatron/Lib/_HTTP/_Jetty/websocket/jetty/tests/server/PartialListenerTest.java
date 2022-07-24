package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging.StacklessLogging;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.DefaultHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.HandlerList;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.RemoteEndpoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPartialListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.util.WSURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.TextUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.WebSocketSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketCreator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.CloseTrackingEndpoint;

import static java.util.concurrent.TimeUnit.SECONDS;
import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

public class PartialListenerTest
{
    private Server server;
    private PartialCreator partialCreator;
    private WebSocketClient client;

    @BeforeEach
    public void startServer() throws Exception
    {
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        ServletHolder closeEndpoint = new ServletHolder(new JettyWebSocketServlet()
        {
            @Override
            public void configure(JettyWebSocketServletFactory factory)
            {
                factory.setIdleTimeout(Duration.ofSeconds(2));
                partialCreator = new PartialCreator();
                factory.setCreator(partialCreator);
            }
        });
        context.addServlet(closeEndpoint, "/ws");
        JettyWebSocketServletContainerInitializer.configure(context, null);

        server.setHandler(new HandlerList(context, new DefaultHandler()));

        server.start();
    }

    @AfterEach
    public void stopServer() throws Exception
    {
        server.stop();
    }

    @BeforeEach
    public void startClient() throws Exception
    {
        client = new WebSocketClient();
        client.start();
    }

    @AfterEach
    public void stopClient() throws Exception
    {
        client.stop();
    }

    private void close(Session session)
    {
        if (session != null)
        {
            session.close();
        }
    }

    @Test
    public void testPartialText() throws Exception
    {
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        CloseTrackingEndpoint clientEndpoint = new CloseTrackingEndpoint();

        URI wsUri = WSURI.toWebsocket(server.getURI().resolve("/ws"));
        Future<Session> futSession = client.connect(clientEndpoint, wsUri, request);

        Session session = null;
        try (StacklessLogging ignore = new StacklessLogging(WebSocketSession.class))
        {
            session = futSession.get(5, SECONDS);

            RemoteEndpoint clientRemote = session.getRemote();
            clientRemote.sendPartialString("hello", false);
            clientRemote.sendPartialString(" ", false);
            clientRemote.sendPartialString("world", true);

            PartialEndpoint serverEndpoint = partialCreator.partialEndpoint;

            String event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload=hello, fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload= , fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload=world, fin=true]"));
        }
        finally
        {
            close(session);
        }
    }

    @Test
    public void testPartialBinary() throws Exception
    {
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        CloseTrackingEndpoint clientEndpoint = new CloseTrackingEndpoint();

        URI wsUri = WSURI.toWebsocket(server.getURI().resolve("/ws"));
        Future<Session> futSession = client.connect(clientEndpoint, wsUri, request);

        Session session = null;
        try (StacklessLogging ignore = new StacklessLogging(WebSocketSession.class))
        {
            session = futSession.get(5, SECONDS);

            RemoteEndpoint clientRemote = session.getRemote();
            clientRemote.sendPartialBytes(BufferUtil.toBuffer("hello"), false);
            clientRemote.sendPartialBytes(BufferUtil.toBuffer(" "), false);
            clientRemote.sendPartialBytes(BufferUtil.toBuffer("world"), true);

            PartialEndpoint serverEndpoint = partialCreator.partialEndpoint;

            String event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("BINARY[payload=<<<hello>>>, fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("BINARY[payload=<<< >>>, fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("BINARY[payload=<<<world>>>, fin=true]"));
        }
        finally
        {
            close(session);
        }
    }

    /**
     * Test to ensure that the internal state tracking the partial messages is reset after each complete message.
     */
    @Test
    public void testPartialTextBinaryText() throws Exception
    {
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        CloseTrackingEndpoint clientEndpoint = new CloseTrackingEndpoint();

        URI wsUri = WSURI.toWebsocket(server.getURI().resolve("/ws"));
        Future<Session> futSession = client.connect(clientEndpoint, wsUri, request);

        Session session = null;
        try (StacklessLogging ignore = new StacklessLogging(WebSocketSession.class))
        {
            session = futSession.get(5, SECONDS);

            RemoteEndpoint clientRemote = session.getRemote();
            clientRemote.sendPartialString("hello", false);
            clientRemote.sendPartialString(" ", false);
            clientRemote.sendPartialString("world", true);

            clientRemote.sendPartialBytes(BufferUtil.toBuffer("greetings"), false);
            clientRemote.sendPartialBytes(BufferUtil.toBuffer(" "), false);
            clientRemote.sendPartialBytes(BufferUtil.toBuffer("mars"), true);

            clientRemote.sendPartialString("salutations", false);
            clientRemote.sendPartialString(" ", false);
            clientRemote.sendPartialString("phobos", true);

            PartialEndpoint serverEndpoint = partialCreator.partialEndpoint;

            String event;

            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload=hello, fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload= , fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload=world, fin=true]"));

            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("BINARY[payload=<<<greetings>>>, fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("BINARY[payload=<<< >>>, fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("BINARY[payload=<<<mars>>>, fin=true]"));

            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload=salutations, fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload= , fin=false]"));
            event = serverEndpoint.partialEvents.poll(5, SECONDS);
            assertThat("Event", event, is("TEXT[payload=phobos, fin=true]"));
        }
        finally
        {
            close(session);
        }
    }

    public static class PartialCreator implements JettyWebSocketCreator
    {
        public PartialEndpoint partialEndpoint;

        @Override
        public Object createWebSocket(JettyServerUpgradeRequest req, JettyServerUpgradeResponse resp)
        {
            partialEndpoint = new PartialEndpoint();
            return partialEndpoint;
        }
    }

    public static class PartialEndpoint implements WebSocketPartialListener
    {
        public Session session;
        public CountDownLatch closeLatch = new CountDownLatch(1);
        public LinkedBlockingQueue<String> partialEvents = new LinkedBlockingQueue<>();

        @Override
        public void onWebSocketClose(int statusCode, String reason)
        {
            closeLatch.countDown();
        }

        @Override
        public void onWebSocketConnect(Session session)
        {
            this.session = session;
        }

        @Override
        public void onWebSocketError(Throwable cause)
        {
            cause.printStackTrace(System.err);
        }

        @Override
        public void onWebSocketPartialBinary(ByteBuffer payload, boolean fin)
        {
            // our testcases always send bytes limited in the US-ASCII range.
            partialEvents.offer(String.format("BINARY[payload=<<<%s>>>, fin=%b]", BufferUtil.toUTF8String(payload), fin));
        }

        @Override
        public void onWebSocketPartialText(String payload, boolean fin)
        {
            partialEvents.offer(String.format("TEXT[payload=%s, fin=%b]", TextUtils.maxStringLength(30, payload), fin));
        }
    }
}