package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BlockingArrayQueue;

@ServerEndpoint("/")
@ClientEndpoint()
public class EventSocket
{
    private static final Logger LOG = LoggerFactory.getLogger(EventSocket.class);

    public Session session;
    public EndpointConfig endpointConfig;

    public BlockingQueue<String> textMessages = new BlockingArrayQueue<>();
    public BlockingQueue<ByteBuffer> binaryMessages = new BlockingArrayQueue<>();
    public volatile Throwable error = null;
    public volatile CloseReason closeReason = null;

    public CountDownLatch openLatch = new CountDownLatch(1);
    public CountDownLatch closeLatch = new CountDownLatch(1);
    public CountDownLatch errorLatch = new CountDownLatch(1);

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig)
    {
        this.session = session;
        this.endpointConfig = endpointConfig;
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onOpen(): {}", toString(), session);
        openLatch.countDown();
    }

    @OnMessage
    public void onMessage(String message) throws IOException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onMessage(): {}", toString(), message);
        textMessages.offer(message);
    }

    @OnMessage
    public void onMessage(ByteBuffer message) throws IOException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onMessage(): {}", toString(), message);
        binaryMessages.offer(message);
    }

    @OnClose
    public void onClose(CloseReason reason)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onClose(): {}", toString(), reason);

        closeReason = reason;
        closeLatch.countDown();
    }

    @OnError
    public void onError(Throwable cause)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("{}  onError(): {}", toString(), cause);
        error = cause;
        errorLatch.countDown();
    }
}