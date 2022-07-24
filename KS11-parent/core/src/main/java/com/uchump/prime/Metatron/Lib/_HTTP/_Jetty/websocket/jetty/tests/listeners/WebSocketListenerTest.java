package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.listeners;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BlockingArrayQueue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EchoSocket;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EventSocket;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebSocketListenerTest
{
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
        JettyWebSocketServletContainerInitializer.configure(contextHandler, (context, container) ->
        {
            container.addMapping("/echo", (req, res) -> new EchoSocket());

            for (Class<?> c : getClassListFromArguments(TextListeners.getTextListeners()))
            {
                container.addMapping("/text/" + c.getSimpleName(), (req, res) -> construct(c));
            }

            for (Class<?> c : getClassListFromArguments(BinaryListeners.getBinaryListeners()))
            {
                container.addMapping("/binary/" + c.getSimpleName(), (req, res) -> construct(c));
            }
        });

        server.setHandler(contextHandler);
        server.start();
        serverUri = URI.create("ws://localhost:" + connector.getLocalPort() + "/");
        client = new WebSocketClient();
        client.start();
    }

    @AfterEach
    public void after() throws Exception
    {
        client.stop();
        server.stop();
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.jetty.websocket.tests.listeners.TextListeners#getTextListeners")
    public void testTextListeners(Class<?> clazz) throws Exception
    {
        EventSocket clientEndpoint = new EventSocket();
        client.connect(clientEndpoint, serverUri.resolve("/text/" + clazz.getSimpleName())).get(5, TimeUnit.SECONDS);

        // Send and receive echo on client.
        String payload = "hello world";
        clientEndpoint.session.getRemote().sendString(payload);
        String echoMessage = clientEndpoint.textMessages.poll(5, TimeUnit.SECONDS);
        assertThat(echoMessage, is(payload));

        // Close normally.
        clientEndpoint.session.close(StatusCode.NORMAL, "standard close");
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.closeCode, is(StatusCode.NORMAL));
        assertThat(clientEndpoint.closeReason, is("standard close"));
    }

    @ParameterizedTest
    @MethodSource("org.eclipse.jetty.websocket.tests.listeners.BinaryListeners#getBinaryListeners")
    public void testBinaryListeners(Class<?> clazz) throws Exception
    {
        EventSocket clientEndpoint = new EventSocket();
        client.connect(clientEndpoint, serverUri.resolve("/binary/" + clazz.getSimpleName())).get(5, TimeUnit.SECONDS);

        // Send and receive echo on client.
        ByteBuffer payload = BufferUtil.toBuffer("hello world");
        clientEndpoint.session.getRemote().sendBytes(payload);
        ByteBuffer echoMessage = clientEndpoint.binaryMessages.poll(5, TimeUnit.SECONDS);
        assertThat(echoMessage, is(payload));

        // Close normally.
        clientEndpoint.session.close(StatusCode.NORMAL, "standard close");
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.closeCode, is(StatusCode.NORMAL));
        assertThat(clientEndpoint.closeReason, is("standard close"));
    }

    @Test
    public void testAnonymousListener() throws Exception
    {
        CountDownLatch openLatch = new CountDownLatch(1);
        CountDownLatch closeLatch = new CountDownLatch(1);
        BlockingQueue<String> textMessages = new BlockingArrayQueue<>();
        WebSocketListener clientEndpoint = new WebSocketListener()
        {
            @Override
            public void onWebSocketConnect(Session session)
            {
                openLatch.countDown();
            }

            @Override
            public void onWebSocketText(String message)
            {
                textMessages.add(message);
            }

            @Override
            public void onWebSocketClose(int statusCode, String reason)
            {
                closeLatch.countDown();
            }
        };

        Session session = client.connect(clientEndpoint, serverUri.resolve("/echo")).get(5, TimeUnit.SECONDS);
        assertTrue(openLatch.await(5, TimeUnit.SECONDS));

        // Send and receive echo on client.
        String payload = "hello world";
        session.getRemote().sendString(payload);
        String echoMessage = textMessages.poll(5, TimeUnit.SECONDS);
        assertThat(echoMessage, is(payload));

        // Close normally.
        session.close(StatusCode.NORMAL, "standard close");
        assertTrue(closeLatch.await(5, TimeUnit.SECONDS));
    }

    private List<Class<?>> getClassListFromArguments(Stream<Arguments> stream)
    {
        return stream.map(arguments -> (Class<?>)arguments.get()[0]).collect(Collectors.toList());
    }

    private <T> T construct(Class<T> clazz)
    {
        try
        {
            @SuppressWarnings("unchecked")
            T instance = (T)clazz.getConstructors()[0].newInstance();
            return instance;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}