package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.WebSocketCoreClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrameBufferTest extends WebSocketTester
{
    private WebSocketServer server;
    private final TestFrameHandler serverHandler = new TestFrameHandler();
    private WebSocketCoreClient client;
    private final WebSocketComponents components = new WebSocketComponents();

    @BeforeEach
    public void startup() throws Exception
    {
        WebSocketNegotiator negotiator = new TestWebSocketNegotiator(serverHandler);
        server = new WebSocketServer(negotiator);
        client = new WebSocketCoreClient(null, components);

        server.start();
        client.start();
    }

    @AfterEach
    public void shutdown() throws Exception
    {
        server.start();
        client.start();
    }

    @Test
    public void testSingleFrame() throws Exception
    {
        TestFrameHandler clientHandler = new TestFrameHandler();
        CompletableFuture<CoreSession> connect = client.connect(clientHandler, server.getUri());
        connect.get(5, TimeUnit.SECONDS);

        ByteBuffer message = BufferUtil.toBuffer("hello world");
        ByteBuffer sendPayload = BufferUtil.copy(message);
        clientHandler.sendFrame(new Frame(OpCode.BINARY, sendPayload), Callback.NOOP, false);

        Frame frame = Objects.requireNonNull(serverHandler.receivedFrames.poll(5, TimeUnit.SECONDS));

        assertThat(frame.getOpCode(), is(OpCode.BINARY));
        assertThat(frame.getPayload(), is(message));
        assertThat(sendPayload, is(message));

        clientHandler.sendClose();
        assertTrue(clientHandler.closed.await(5, TimeUnit.SECONDS));
        assertNull(clientHandler.getError());
    }

    @Test
    public void testSendSameFrameMultipleTimes() throws Exception
    {
        TestFrameHandler clientHandler = new TestFrameHandler();
        client.connect(clientHandler, server.getUri()).get(5, TimeUnit.SECONDS);
        serverHandler.open.await(5, TimeUnit.SECONDS);
        clientHandler.coreSession.setAutoFragment(false);
        serverHandler.coreSession.setAutoFragment(false);

        int payloadLen = 32 * 1024;
        byte[] array = new byte[payloadLen];
        new Random().nextBytes(array);
        ByteBuffer message = ByteBuffer.wrap(array);

        Frame frame = new Frame(OpCode.BINARY, BufferUtil.copy(message));
        for (int i = 0; i < 200; i++)
        {
            clientHandler.sendFrame(frame, Callback.NOOP, false);
            Frame recvFrame = Objects.requireNonNull(serverHandler.receivedFrames.poll(5, TimeUnit.SECONDS));
            assertThat(recvFrame.getOpCode(), is(OpCode.BINARY));
            assertThat(recvFrame.getPayload(), is(message));
            assertThat(frame.getPayload(), is(message));
        }

        clientHandler.sendClose();
        assertTrue(clientHandler.closed.await(5, TimeUnit.SECONDS));
        assertNull(clientHandler.getError());
    }
}