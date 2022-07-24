package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BlockingArrayQueue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketClose;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketConnect;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketError;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class EventSocket
{
    private static final Logger LOG = LoggerFactory.getLogger(EventSocket.class);

    public Session session;
    private String behavior;

    public BlockingQueue<String> textMessages = new BlockingArrayQueue<>();
    public BlockingQueue<ByteBuffer> binaryMessages = new BlockingArrayQueue<>();
    public volatile int closeCode = StatusCode.UNDEFINED;
    public volatile String closeReason;
    public volatile Throwable error = null;

    public CountDownLatch openLatch = new CountDownLatch(1);
    public CountDownLatch errorLatch = new CountDownLatch(1);
    public CountDownLatch closeLatch = new CountDownLatch(1);

    @OnWebSocketConnect
    public void onOpen(Session session)
    {
        this.session = session;
        behavior = session.getPolicy().getBehavior().name();
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onOpen(): {}", toString(), session);
        openLatch.countDown();
    }

    @OnWebSocketMessage
    public void onMessage(String message) throws IOException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onMessage(): {}", toString(), message);
        textMessages.offer(message);
    }

    @OnWebSocketMessage
    public void onMessage(byte[] buf, int offset, int len) throws IOException
    {
        ByteBuffer message = ByteBuffer.wrap(buf, offset, len);
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onMessage(): {}", toString(), message);
        binaryMessages.offer(message);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onClose(): {}:{}", toString(), statusCode, reason);
        this.closeCode = statusCode;
        this.closeReason = reason;
        closeLatch.countDown();
    }

    @OnWebSocketError
    public void onError(Throwable cause)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onError(): {}", toString(), cause);
        error = cause;
        errorLatch.countDown();
    }

    @Override
    public String toString()
    {
        return String.format("[%s@%s]", behavior, Integer.toHexString(hashCode()));
    }
}