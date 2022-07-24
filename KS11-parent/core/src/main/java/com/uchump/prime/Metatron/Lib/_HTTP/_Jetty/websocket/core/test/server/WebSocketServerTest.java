package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.server;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BlockingArrayQueue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.RawFrameBuilder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.TestFrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.WebSocketServer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.WebSocketTester;
import com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests of a core server with a fake client
 */
public class WebSocketServerTest extends WebSocketTester
{
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerTest.class);

    private WebSocketServer server;

    @Test
    public void testHelloEcho() throws Exception
    {
        TestFrameHandler serverHandler = new TestFrameHandler()
        {
            public void onFrame(Frame frame, Callback callback)
            {
                getCoreSession().sendFrame(Frame.copy(frame), Callback.NOOP, false);
                super.onFrame(frame, callback);
            }
        };

        server = new WebSocketServer(serverHandler);
        server.start();

        try (Socket client = newClient(server.getLocalPort()))
        {
            client.getOutputStream().write(RawFrameBuilder.buildText("Hello!", true));

            Frame frame = serverHandler.receivedFrames.poll(10, TimeUnit.SECONDS);
            assertNotNull(frame);

            frame = receiveFrame(client.getInputStream());
            assertNotNull(frame);
            assertThat(frame.getPayloadAsUTF8(), is("Hello!"));

            client.getOutputStream().write(RawFrameBuilder.buildClose(CloseStatus.NORMAL_STATUS, true));
            assertTrue(serverHandler.closed.await(5, TimeUnit.SECONDS));
            frame = receiveFrame(client.getInputStream());
            assertNotNull(frame);
            MatcherAssert.assertThat(frame.getOpCode(), Matchers.is(OpCode.CLOSE));
            assertThat(new CloseStatus(frame.getPayload()).getCode(), is(CloseStatus.NORMAL));
        }
    }

    @Test
    public void testSimpleDemand() throws Exception
    {
        TestFrameHandler serverHandler = new TestFrameHandler()
        {
            @Override
            public boolean isDemanding()
            {
                return true;
            }
        };

        server = new WebSocketServer(serverHandler);
        server.start();

        try (Socket client = newClient(server.getLocalPort()))
        {
            client.getOutputStream().write(RawFrameBuilder.buildText("Hello", true));

            Frame frame = serverHandler.receivedFrames.poll(250, TimeUnit.MILLISECONDS);
            assertNull(frame);

            serverHandler.getCoreSession().demand(2);

            frame = serverHandler.receivedFrames.poll(10, TimeUnit.SECONDS);
            assertNotNull(frame);
            assertThat(frame.getPayloadAsUTF8(), is("Hello"));

            client.getOutputStream().write(RawFrameBuilder.buildText("World", true));
            frame = serverHandler.receivedFrames.poll(10, TimeUnit.SECONDS);
            assertNotNull(frame);
            assertThat(frame.getPayloadAsUTF8(), is("World"));

            client.getOutputStream().write(RawFrameBuilder.buildClose(CloseStatus.NORMAL_STATUS, true));
            assertFalse(serverHandler.closed.await(250, TimeUnit.MILLISECONDS));

            serverHandler.getCoreSession().demand(1);
            assertTrue(serverHandler.closed.await(10, TimeUnit.SECONDS));
            frame = serverHandler.receivedFrames.poll(10, TimeUnit.SECONDS);
            assertNotNull(frame);
            assertThat(frame.getOpCode(), is(OpCode.CLOSE));

            frame = receiveFrame(client.getInputStream());
            assertNotNull(frame);
            assertThat(frame.getOpCode(), is(OpCode.CLOSE));
            assertThat(new CloseStatus(frame.getPayload()).getCode(), is(CloseStatus.NORMAL));
        }
    }

    @Test
    public void testDemandAndRetain() throws Exception
    {
        BlockingQueue<Callback> receivedCallbacks = new BlockingArrayQueue<>();

        TestFrameHandler serverHandler = new TestFrameHandler()
        {
            @Override
            public void onOpen(CoreSession coreSession, Callback callback)
            {
                super.onOpen(coreSession);
                callback.succeeded();
                coreSession.demand(1);
            }

            @Override
            public boolean isDemanding()
            {
                return true;
            }

            @Override
            public void onFrame(Frame frame, Callback callback)
            {
                LOG.info("onFrame: " + BufferUtil.toDetailString(frame.getPayload()));
                receivedFrames.offer(frame);
                receivedCallbacks.offer(callback);
                getCoreSession().demand(1);
            }
        };

        server = new WebSocketServer(serverHandler);
        server.start();

        try (Socket client = newClient(server.getLocalPort()))
        {
            ByteBuffer buffer = BufferUtil.allocate(4096);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("Hello", true), 0, 6 + 5);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("Cruel", true), 0, 6 + 5);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("World", true), 0, 6 + 5);
            client.getOutputStream().write(BufferUtil.toArray(buffer));

            long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            while (serverHandler.receivedFrames.size() < 3)
            {
                assertThat(System.nanoTime(), Matchers.lessThan(end));
                Thread.sleep(10);
            }
            assertThat(serverHandler.receivedFrames.size(), is(3));
            assertThat(receivedCallbacks.size(), is(3));

            BufferUtil.clear(buffer);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("Good", true), 0, 6 + 4);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("Bye", true), 0, 6 + 3);
            client.getOutputStream().write(BufferUtil.toArray(buffer));

            end = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            while (serverHandler.receivedFrames.size() < 5)
            {
                assertThat(System.nanoTime(), Matchers.lessThan(end));
                Thread.sleep(10);
            }
            assertThat(serverHandler.receivedFrames.size(), is(5));
            assertThat(receivedCallbacks.size(), is(5));
        }
    }

    @Test
    public void testBadOpCode() throws Exception
    {
        TestFrameHandler serverHandler = new TestFrameHandler();

        server = new WebSocketServer(serverHandler);
        server.start();

        try (Socket client = newClient(server.getLocalPort()))
        {
            client.getOutputStream().write(RawFrameBuilder.buildFrame((byte)4, "payload", true));
            assertTrue(serverHandler.closed.await(5, TimeUnit.SECONDS));

            Frame frame = receiveFrame(client.getInputStream());
            assertNotNull(frame);
            assertThat(frame.getOpCode(), is(OpCode.CLOSE));
            assertThat(new CloseStatus(frame.getPayload()).getCode(), is(CloseStatus.PROTOCOL));
        }
    }

    @Test
    public void testBadClose() throws Exception
    {
        TestFrameHandler serverHandler = new TestFrameHandler();

        server = new WebSocketServer(serverHandler);
        server.start();

        try (Socket client = newClient(server.getLocalPort()))
        {
            // Write client close without masking!
            client.getOutputStream().write(RawFrameBuilder.buildClose(CloseStatus.NORMAL_STATUS, false));
            assertTrue(serverHandler.closed.await(5, TimeUnit.SECONDS));
            Frame frame = receiveFrame(client.getInputStream());
            assertNotNull(frame);
            assertThat(frame.getOpCode(), is(OpCode.CLOSE));
            assertThat(new CloseStatus(frame.getPayload()).getCode(), is(CloseStatus.PROTOCOL));
        }
    }

    @Test
    public void testTcpCloseNoDemand() throws Exception
    {
        BlockingQueue<Callback> receivedCallbacks = new BlockingArrayQueue<>();

        TestFrameHandler serverHandler = new TestFrameHandler()
        {
            @Override
            public void onOpen(CoreSession coreSession, Callback callback)
            {
                super.onOpen(coreSession);
                callback.succeeded();
                coreSession.demand(3);
            }

            @Override
            public boolean isDemanding()
            {
                return true;
            }

            @Override
            public void onFrame(Frame frame, Callback callback)
            {
                LOG.info("onFrame: " + BufferUtil.toDetailString(frame.getPayload()));
                receivedFrames.offer(frame);
                receivedCallbacks.offer(callback);
            }
        };

        server = new WebSocketServer(serverHandler);
        server.start();

        try (Socket client = newClient(server.getLocalPort()))
        {
            ByteBuffer buffer = BufferUtil.allocate(4096);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("Hello", true), 0, 6 + 5);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("Cruel", true), 0, 6 + 5);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("World", true), 0, 6 + 5);
            client.getOutputStream().write(BufferUtil.toArray(buffer));

            long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            while (serverHandler.receivedFrames.size() < 3)
            {
                assertThat(System.nanoTime(), Matchers.lessThan(end));
                Thread.sleep(10);
            }
            assertThat(serverHandler.receivedFrames.size(), is(3));
            assertThat(receivedCallbacks.size(), is(3));

            client.close();

            assertFalse(serverHandler.closed.await(250, TimeUnit.MILLISECONDS));
            serverHandler.getCoreSession().demand(1);
            assertTrue(serverHandler.closed.await(10, TimeUnit.SECONDS));
        }
    }

    @Test
    public void testHandlerClosed() throws Exception
    {
        BlockingQueue<Callback> receivedCallbacks = new BlockingArrayQueue<>();
        AtomicReference<CloseStatus> closedStatus = new AtomicReference<>();

        TestFrameHandler serverHandler = new TestFrameHandler()
        {
            @Override
            public void onOpen(CoreSession coreSession, Callback callback)
            {
                super.onOpen(coreSession);
                callback.succeeded();
                coreSession.demand(2);
            }

            @Override
            public void onClosed(CloseStatus closeStatus)
            {
                closedStatus.set(closeStatus);
                super.onClosed(closeStatus);
            }

            @Override
            public boolean isDemanding()
            {
                return true;
            }

            @Override
            public void onFrame(Frame frame, Callback callback)
            {
                LOG.info("onFrame: " + BufferUtil.toDetailString(frame.getPayload()));
                receivedFrames.offer(frame);
                receivedCallbacks.offer(callback);
            }
        };

        server = new WebSocketServer(serverHandler);
        server.start();

        try (Socket client = newClient(server.getLocalPort()))
        {
            ByteBuffer buffer = BufferUtil.allocate(4096);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("Hello", true));
            BufferUtil.append(buffer, RawFrameBuilder.buildClose(CloseStatus.NORMAL_STATUS, true));
            client.getOutputStream().write(BufferUtil.toArray(buffer));

            long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            while (serverHandler.receivedFrames.size() < 2)
            {
                assertThat(System.nanoTime(), Matchers.lessThan(end));
                Thread.sleep(10);
            }
            assertThat(serverHandler.receivedFrames.size(), is(2));
            assertThat(receivedCallbacks.size(), is(2));

            assertThat(serverHandler.receivedFrames.poll().getPayloadAsUTF8(), is("Hello"));
            receivedCallbacks.poll().succeeded();

            serverHandler.getCoreSession().sendFrame(CloseStatus.toFrame(CloseStatus.SHUTDOWN, "Test Close"), Callback.NOOP, false);

            Frame frame = receiveFrame(client.getInputStream());
            assertNotNull(frame);
            assertThat(frame.getOpCode(), is(OpCode.CLOSE));
            assertThat(new CloseStatus(frame.getPayload()).getCode(), is(CloseStatus.SHUTDOWN));

            assertTrue(serverHandler.closed.await(10, TimeUnit.SECONDS));
            assertThat(closedStatus.get().getCode(), is(CloseStatus.SHUTDOWN));

            assertThat(serverHandler.receivedFrames.poll().getOpCode(), is(OpCode.CLOSE));
            receivedCallbacks.poll().succeeded();
        }
    }

    @Test
    public void testDelayedClosed() throws Exception
    {
        BlockingQueue<Callback> receivedCallbacks = new BlockingArrayQueue<>();

        TestFrameHandler serverHandler = new TestFrameHandler()
        {
            @Override
            public void onOpen(CoreSession coreSession, Callback callback)
            {
                super.onOpen(coreSession);
                callback.succeeded();
                coreSession.demand(2);
            }

            @Override
            public boolean isDemanding()
            {
                return true;
            }

            @Override
            public void onFrame(Frame frame, Callback callback)
            {
                LOG.info("onFrame: " + BufferUtil.toDetailString(frame.getPayload()));
                receivedFrames.offer(frame);
                receivedCallbacks.offer(callback);
            }
        };

        server = new WebSocketServer(serverHandler);
        server.start();

        try (Socket client = newClient(server.getLocalPort()))
        {
            ByteBuffer buffer = BufferUtil.allocate(4096);
            BufferUtil.append(buffer, RawFrameBuilder.buildText("Hello", true));
            BufferUtil.append(buffer, RawFrameBuilder.buildClose(CloseStatus.NORMAL_STATUS, true));
            client.getOutputStream().write(BufferUtil.toArray(buffer));

            long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            while (serverHandler.receivedFrames.size() < 2)
            {
                assertThat(System.nanoTime(), Matchers.lessThan(end));
                Thread.sleep(10);
            }
            assertThat(serverHandler.receivedFrames.size(), is(2));
            assertThat(receivedCallbacks.size(), is(2));

            assertThat(serverHandler.receivedFrames.poll().getPayloadAsUTF8(), is("Hello"));
            receivedCallbacks.poll().succeeded();

            serverHandler.getCoreSession().sendFrame(new Frame(OpCode.TEXT, "Ciao"), Callback.NOOP, false);

            Frame frame = receiveFrame(client.getInputStream());
            assertNotNull(frame);
            assertThat(frame.getPayloadAsUTF8(), is("Ciao"));

            assertThat(serverHandler.receivedFrames.poll().getOpCode(), is(OpCode.CLOSE));
            receivedCallbacks.poll().succeeded();

            frame = receiveFrame(client.getInputStream());
            assertNotNull(frame);
            assertThat(frame.getOpCode(), is(OpCode.CLOSE));
        }
    }
}