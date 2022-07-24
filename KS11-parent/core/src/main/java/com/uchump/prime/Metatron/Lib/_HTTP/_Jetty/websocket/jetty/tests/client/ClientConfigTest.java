package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.client;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.BatchMode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions.MessageTooLargeException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions.WebSocketTimeoutException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.WebSocketSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EchoSocket;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EventSocket;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientConfigTest
{
    private Server server;
    private WebSocketClient client;
    private ServerConnector connector;
    private final EchoSocket serverSocket = new EchoSocket();

    private static final String MESSAGE = "this message is over 20 characters long";
    private static final int INPUT_BUFFER_SIZE = 200;
    private static final int MAX_MESSAGE_SIZE = 20;
    private static final int IDLE_TIMEOUT = 500;

    public static Stream<Arguments> data()
    {
        return Stream.of("clientConfig", "annotatedConfig", "sessionConfig").map(Arguments::of);
    }

    @BeforeEach
    public void start() throws Exception
    {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);

        JettyWebSocketServletContainerInitializer.configure(contextHandler,
            (context, container) -> container.addMapping("/", (req, resp) -> serverSocket));

        server.start();

        client = new WebSocketClient();
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        client.stop();
        server.stop();
    }

    @WebSocket(idleTimeout = IDLE_TIMEOUT, maxTextMessageSize = MAX_MESSAGE_SIZE, maxBinaryMessageSize = MAX_MESSAGE_SIZE, inputBufferSize = INPUT_BUFFER_SIZE, batchMode = BatchMode.ON)
    public static class AnnotatedConfigEndpoint extends EventSocket
    {
    }

    @WebSocket
    public static class SessionConfigEndpoint extends EventSocket
    {
        @Override
        public void onOpen(Session session)
        {
            session.setIdleTimeout(Duration.ofMillis(IDLE_TIMEOUT));
            session.setMaxTextMessageSize(MAX_MESSAGE_SIZE);
            session.setMaxBinaryMessageSize(MAX_MESSAGE_SIZE);
            session.setInputBufferSize(INPUT_BUFFER_SIZE);
            super.onOpen(session);
        }
    }

    public EventSocket getClientSocket(String param)
    {
        switch (param)
        {
            case "clientConfig":
                client.setInputBufferSize(INPUT_BUFFER_SIZE);
                client.setMaxBinaryMessageSize(MAX_MESSAGE_SIZE);
                client.setIdleTimeout(Duration.ofMillis(IDLE_TIMEOUT));
                client.setMaxTextMessageSize(MAX_MESSAGE_SIZE);
                return new EventSocket();

            case "annotatedConfig":
                return new AnnotatedConfigEndpoint();

            case "sessionConfig":
                return new SessionConfigEndpoint();

            default:
                throw new IllegalStateException();
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testInputBufferSize(String param) throws Exception
    {
        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/");
        EventSocket clientEndpoint = getClientSocket(param);
        CompletableFuture<Session> connect = client.connect(clientEndpoint, uri);

        connect.get(5, TimeUnit.SECONDS);

        WebSocketCoreSession coreSession = (WebSocketCoreSession)((WebSocketSession)clientEndpoint.session).getCoreSession();
        WebSocketConnection connection = coreSession.getConnection();

        assertThat(connection.getInputBufferSize(), is(INPUT_BUFFER_SIZE));

        clientEndpoint.session.close();
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertNull(clientEndpoint.error);

        assertTrue(serverSocket.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(serverSocket.closeCode, is(StatusCode.NORMAL));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testMaxBinaryMessageSize(String param) throws Exception
    {
        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/");
        EventSocket clientEndpoint = getClientSocket(param);
        CompletableFuture<Session> connect = client.connect(clientEndpoint, uri);

        connect.get(5, TimeUnit.SECONDS);
        clientEndpoint.session.getRemote().sendBytes(BufferUtil.toBuffer(MESSAGE));
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));

        assertThat(clientEndpoint.error, instanceOf(MessageTooLargeException.class));

        assertTrue(serverSocket.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(serverSocket.closeCode, is(StatusCode.MESSAGE_TOO_LARGE));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIdleTimeout(String param) throws Exception
    {
        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/");
        EventSocket clientEndpoint = getClientSocket(param);
        CompletableFuture<Session> connect = client.connect(clientEndpoint, uri);

        connect.get(5, TimeUnit.SECONDS);
        clientEndpoint.session.getRemote().sendString("hello world");
        Thread.sleep(IDLE_TIMEOUT + 500);

        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.error, instanceOf(WebSocketTimeoutException.class));

        assertTrue(serverSocket.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(serverSocket.closeCode, is(StatusCode.SHUTDOWN));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testMaxTextMessageSize(String param) throws Exception
    {
        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/");
        EventSocket clientEndpoint = getClientSocket(param);
        CompletableFuture<Session> connect = client.connect(clientEndpoint, uri);

        connect.get(5, TimeUnit.SECONDS);
        clientEndpoint.session.getRemote().sendString(MESSAGE);
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));

        assertThat(clientEndpoint.error, instanceOf(MessageTooLargeException.class));

        assertTrue(serverSocket.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(serverSocket.closeCode, is(StatusCode.MESSAGE_TOO_LARGE));
    }
}