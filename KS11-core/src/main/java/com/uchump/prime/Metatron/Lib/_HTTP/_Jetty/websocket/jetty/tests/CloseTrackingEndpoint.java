package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketAdapter;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.WebSocketSession;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.nullValue;

public class CloseTrackingEndpoint extends WebSocketAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(CloseTrackingEndpoint.class);

    public int closeCode = -1;
    public String closeReason = null;
    public CountDownLatch closeLatch = new CountDownLatch(1);
    public AtomicInteger closeCount = new AtomicInteger(0);
    public CountDownLatch openLatch = new CountDownLatch(1);
    public CountDownLatch errorLatch = new CountDownLatch(1);

    public LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    public LinkedBlockingQueue<ByteBuffer> binaryMessageQueue = new LinkedBlockingQueue<>();
    public AtomicReference<Throwable> error = new AtomicReference<>();

    public void assertReceivedCloseEvent(int clientTimeoutMs, Matcher<Integer> statusCodeMatcher)
        throws InterruptedException
    {
        assertReceivedCloseEvent(clientTimeoutMs, statusCodeMatcher, null);
    }

    public void assertReceivedCloseEvent(int clientTimeoutMs, Matcher<Integer> statusCodeMatcher, Matcher<String> reasonMatcher)
        throws InterruptedException
    {
        assertThat("Client Close Event Occurred", closeLatch.await(clientTimeoutMs, TimeUnit.MILLISECONDS), is(true));
        assertThat("Client Close Event Count", closeCount.get(), is(1));
        assertThat("Client Close Event Status Code", closeCode, statusCodeMatcher);
        if (reasonMatcher == null)
        {
            assertThat("Client Close Event Reason", closeReason, nullValue());
        }
        else
        {
            assertThat("Client Close Event Reason", closeReason, reasonMatcher);
        }
    }

    public void clearQueues()
    {
        messageQueue.clear();
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        LOG.debug("onWebSocketClose({},{})", statusCode, reason);
        super.onWebSocketClose(statusCode, reason);
        closeCount.incrementAndGet();
        closeCode = statusCode;
        closeReason = reason;
        closeLatch.countDown();
    }

    @Override
    public void onWebSocketConnect(Session session)
    {
        LOG.debug("onWebSocketConnect({})", session);
        super.onWebSocketConnect(session);
        openLatch.countDown();
    }

    @Override
    public void onWebSocketError(Throwable cause)
    {
        LOG.debug("onWebSocketError", cause);
        assertThat("Unique Error Event", error.compareAndSet(null, cause), is(true));
        errorLatch.countDown();
    }

    @Override
    public void onWebSocketText(String message)
    {
        LOG.debug("onWebSocketText({})", message);
        messageQueue.offer(message);
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len)
    {
        LOG.debug("onWebSocketBinary({},{},{})", payload, offset, len);
        binaryMessageQueue.offer(ByteBuffer.wrap(payload, offset, len));
    }

    public EndPoint getEndPoint()
    {
        Session session = getSession();
        assertThat("Session type", session, instanceOf(WebSocketSession.class));

        WebSocketSession wsSession = (WebSocketSession)session;
        WebSocketCoreSession wsCoreSession = (WebSocketCoreSession)wsSession.getCoreSession();
        WebSocketConnection wsConnection = wsCoreSession.getConnection();

        return wsConnection.getEndPoint();
    }
}