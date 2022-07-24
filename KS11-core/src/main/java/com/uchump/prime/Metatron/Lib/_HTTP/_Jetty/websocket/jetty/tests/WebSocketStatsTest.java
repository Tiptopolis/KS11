package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.lang.management.ManagementFactory;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.IncludeExcludeConnectionStatistics;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.MBeanContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Generator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebSocketStatsTest
{
    private final CountDownLatch wsConnectionClosed = new CountDownLatch(1);
    private Server server;
    private ServerConnector connector;
    private WebSocketClient client;
    private IncludeExcludeConnectionStatistics statistics;

    @BeforeEach
    public void start() throws Exception
    {
        statistics = new IncludeExcludeConnectionStatistics();
        statistics.include(WebSocketConnection.class);

        Connection.Listener.Adapter wsCloseListener = new Connection.Listener.Adapter()
        {
            @Override
            public void onClosed(Connection connection)
            {
                if (connection instanceof WebSocketConnection)
                    wsConnectionClosed.countDown();
            }
        };

        server = new Server();
        connector = new ServerConnector(server);
        connector.addBean(statistics);
        connector.addBean(wsCloseListener);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(contextHandler);
        contextHandler.setContextPath("/");
        JettyWebSocketServletContainerInitializer.configure(contextHandler, (context, container) ->
        {
            container.setAutoFragment(false);
            container.addMapping("/", EchoSocket.class);
        });

        JettyWebSocketServletContainerInitializer.configure(contextHandler, null);
        client = new WebSocketClient();
        client.setAutoFragment(false);

        // Setup JMX.
        MBeanContainer mbeanContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbeanContainer);

        server.start();
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        server.stop();
        client.stop();
    }

    long getFrameByteSize(Frame frame)
    {
        Generator generator = new Generator();
        ByteBuffer headerBuffer = BufferUtil.allocate(Generator.MAX_HEADER_LENGTH);
        generator.generateHeader(frame, headerBuffer);
        return headerBuffer.remaining() + frame.getPayloadLength();
    }

    @Test
    public void echoStatsTest() throws Exception
    {
        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/");
        EventSocket socket = new EventSocket();
        CompletableFuture<Session> connect = client.connect(socket, uri);

        final long numMessages = 1000;
        final String msgText = "hello world";
        try (Session session = connect.get(5, TimeUnit.SECONDS))
        {
            for (int i = 0; i < numMessages; i++)
            {
                session.getRemote().sendString(msgText);
            }
        }
        assertTrue(socket.closeLatch.await(5, TimeUnit.SECONDS));
        assertTrue(wsConnectionClosed.await(5, TimeUnit.SECONDS));

        assertThat(statistics.getConnectionsMax(), is(1L));
        assertThat(statistics.getConnections(), is(0L));

        // Sent and r eceived all of the echo messages + 1 for the close frame.
        assertThat(statistics.getSentMessages(), is(numMessages + 1L));
        assertThat(statistics.getReceivedMessages(), is(numMessages + 1L));

        Frame textFrame = new Frame(OpCode.TEXT, msgText);
        Frame closeFrame = CloseStatus.NORMAL_STATUS.toFrame();
        final long textFrameSize = getFrameByteSize(textFrame);
        final long closeFrameSize = getFrameByteSize(closeFrame);
        final int maskSize = 4; // We use 4 byte mask for client frames in WSConnection

        final long expectedSent =  numMessages * textFrameSize + closeFrameSize;
        final long expectedReceived =  numMessages * (textFrameSize + maskSize) + (closeFrameSize + maskSize);

        assertThat(statistics.getSentBytes(), is(expectedSent));
        assertThat(statistics.getReceivedBytes(), is(expectedReceived));
    }
}