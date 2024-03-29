package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LargeDeflateTest
{
    private Server _server;
    private ServerConnector _connector;
    private WebSocketClient _client;
    private final EventSocket _serverSocket = new EventSocket();

    @BeforeEach
    void before() throws Exception
    {
        _server = new Server();
        _connector = new ServerConnector(_server);
        _server.addConnector(_connector);

        ServletContextHandler handler = new ServletContextHandler();
        _server.insertHandler(handler);
        JettyWebSocketServletContainerInitializer.configure(handler, (servletContext, container) ->
        {
            container.setIdleTimeout(Duration.ofDays(1));
            container.setMaxFrameSize(Integer.MAX_VALUE);
            container.setMaxBinaryMessageSize(Integer.MAX_VALUE);
            container.addMapping("/", (req, resp) -> _serverSocket);
        });

        _server.start();
        _client = new WebSocketClient();
        _client.start();
    }

    @AfterEach
    void after() throws Exception
    {
        _client.stop();
        _server.stop();
    }

    @Test
    void testDeflate() throws Exception
    {
        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        upgradeRequest.addExtensions("permessage-deflate");

        EventSocket clientSocket = new EventSocket();
        Session session = _client.connect(clientSocket, URI.create("ws://localhost:" + _connector.getLocalPort() + "/ws"), upgradeRequest).get();
        ByteBuffer sentMessage = largePayloads();
        session.getRemote().sendBytes(sentMessage);
        session.close(StatusCode.NORMAL, "close from test");

        assertTrue(_serverSocket.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(_serverSocket.closeCode, is(StatusCode.NORMAL));
        assertThat(_serverSocket.closeReason, is("close from test"));

        ByteBuffer message = _serverSocket.binaryMessages.poll(1, TimeUnit.SECONDS);
        assertThat(message, is(sentMessage));
    }

    private static ByteBuffer largePayloads()
    {
        var bytes = new byte[4 * 1024 * 1024];
        new Random(42).nextBytes(bytes);
        return BufferUtil.toBuffer(bytes);
    }
}