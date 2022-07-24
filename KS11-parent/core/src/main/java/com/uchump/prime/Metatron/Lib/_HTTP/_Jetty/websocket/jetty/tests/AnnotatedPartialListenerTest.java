package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPartialListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WriteCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions.InvalidWebSocketException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnnotatedPartialListenerTest
{
    public static class PartialEchoSocket implements WebSocketPartialListener
    {
        private Session session;

        @Override
        public void onWebSocketConnect(Session session)
        {
            this.session = session;
        }

        @Override
        public void onWebSocketPartialBinary(ByteBuffer payload, boolean fin)
        {
            session.getRemote().sendPartialBytes(payload, fin, WriteCallback.NOOP);
        }

        @Override
        public void onWebSocketPartialText(String payload, boolean fin)
        {
            try
            {
                session.getRemote().sendPartialString(payload, fin, WriteCallback.NOOP);
            }
            catch (IOException e)
            {
                throw new IllegalStateException(e);
            }
        }
    }

    @WebSocket
    public static class PartialStringListener
    {
        public BlockingQueue<MessageSegment> messages = new LinkedBlockingQueue<>();

        public static class MessageSegment
        {
            public String message;
            public boolean last;
        }

        @OnWebSocketMessage
        public void onMessage(String message, boolean last)
        {
            MessageSegment messageSegment = new MessageSegment();
            messageSegment.message = message;
            messageSegment.last = last;
            messages.add(messageSegment);
        }
    }

    @WebSocket
    public static class PartialByteBufferListener
    {
        public BlockingQueue<MessageSegment> messages = new LinkedBlockingQueue<>();

        public static class MessageSegment
        {
            public ByteBuffer buffer;
            public boolean last;
        }

        @OnWebSocketMessage
        public void onMessage(ByteBuffer buffer, boolean last)
        {
            MessageSegment messageSegment = new MessageSegment();
            messageSegment.buffer = BufferUtil.copy(buffer);
            messageSegment.last = last;
            messages.add(messageSegment);
        }
    }

    @WebSocket
    public static class InvalidDoubleBinaryListener
    {
        @OnWebSocketMessage
        public void onMessage(ByteBuffer bytes, boolean last)
        {
        }

        @OnWebSocketMessage
        public void onMessage(ByteBuffer bytes)
        {
        }
    }

    @WebSocket
    public static class InvalidDoubleTextListener
    {
        @OnWebSocketMessage
        public void onMessage(String content, boolean last)
        {
        }

        @OnWebSocketMessage
        public void onMessage(String content)
        {
        }
    }

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
        JettyWebSocketServletContainerInitializer.configure(contextHandler, ((servletContext, container) ->
        {
            container.setAutoFragment(false);
            container.addMapping("/", PartialEchoSocket.class);
        }));
        server.start();
        serverUri = URI.create("ws://localhost:" + connector.getLocalPort() + "/");

        client = new WebSocketClient();
        client.setAutoFragment(false);
        client.start();
    }

    @AfterEach
    public void after() throws Exception
    {
        client.stop();
        server.stop();
    }

    @Test
    public void testAnnotatedPartialString() throws Exception
    {
        PartialStringListener endpoint = new PartialStringListener();
        try (Session session = client.connect(endpoint, serverUri).get(5, TimeUnit.SECONDS))
        {
            session.getRemote().sendPartialString("hell", false);
            session.getRemote().sendPartialString("o w", false);
            session.getRemote().sendPartialString("orld", true);
        }

        PartialStringListener.MessageSegment segment;

        segment = Objects.requireNonNull(endpoint.messages.poll(5, TimeUnit.SECONDS));
        assertThat(segment.message, is("hell"));
        assertThat(segment.last, is(false));

        segment = Objects.requireNonNull(endpoint.messages.poll(5, TimeUnit.SECONDS));
        assertThat(segment.message, is("o w"));
        assertThat(segment.last, is(false));

        segment = Objects.requireNonNull(endpoint.messages.poll(5, TimeUnit.SECONDS));
        assertThat(segment.message, is("orld"));
        assertThat(segment.last, is(true));
    }

    @Test
    public void testAnnotatedPartialByteBuffer() throws Exception
    {
        PartialByteBufferListener endpoint = new PartialByteBufferListener();
        try (Session session = client.connect(endpoint, serverUri).get(5, TimeUnit.SECONDS))
        {
            session.getRemote().sendPartialBytes(BufferUtil.toBuffer("hell"), false);
            session.getRemote().sendPartialBytes(BufferUtil.toBuffer("o w"), false);
            session.getRemote().sendPartialBytes(BufferUtil.toBuffer("orld"), true);
        }

        PartialByteBufferListener.MessageSegment segment;

        segment = Objects.requireNonNull(endpoint.messages.poll(5, TimeUnit.SECONDS));
        assertThat(segment.buffer, is(BufferUtil.toBuffer("hell")));
        assertThat(segment.last, is(false));

        segment = Objects.requireNonNull(endpoint.messages.poll(5, TimeUnit.SECONDS));
        assertThat(segment.buffer, is(BufferUtil.toBuffer("o w")));
        assertThat(segment.last, is(false));

        segment = Objects.requireNonNull(endpoint.messages.poll(5, TimeUnit.SECONDS));
        assertThat(segment.buffer, is(BufferUtil.toBuffer("orld")));
        assertThat(segment.last, is(true));
    }

    @Test
    public void testDoubleOnMessageAnnotation()
    {
        InvalidDoubleBinaryListener doubleBinaryListener = new InvalidDoubleBinaryListener();
        assertThrows(InvalidWebSocketException.class, () -> client.connect(doubleBinaryListener, serverUri));

        InvalidDoubleTextListener doubleTextListener = new InvalidDoubleTextListener();
        assertThrows(InvalidWebSocketException.class, () -> client.connect(doubleTextListener, serverUri));
    }
}