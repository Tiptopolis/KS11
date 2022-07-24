package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BlockingArrayQueue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.MessageHandler;

public class TestMessageHandler extends MessageHandler
{
    protected static final Logger LOG = LoggerFactory.getLogger(TestMessageHandler.class);

    public CoreSession coreSession;
    public BlockingQueue<String> textMessages = new BlockingArrayQueue<>();
    public BlockingQueue<ByteBuffer> binaryMessages = new BlockingArrayQueue<>();
    public CloseStatus closeStatus;
    public volatile Throwable error;
    public CountDownLatch openLatch = new CountDownLatch(1);
    public CountDownLatch errorLatch = new CountDownLatch(1);
    public CountDownLatch closeLatch = new CountDownLatch(1);

    @Override
    public void onOpen(CoreSession coreSession, Callback callback)
    {
        super.onOpen(coreSession, callback);
        this.coreSession = coreSession;
        openLatch.countDown();
    }

    @Override
    public void onError(Throwable cause, Callback callback)
    {
        super.onError(cause, callback);
        error = cause;
        errorLatch.countDown();
    }

    @Override
    public void onClosed(CloseStatus closeStatus, Callback callback)
    {
        super.onClosed(closeStatus, callback);
        this.closeStatus = closeStatus;
        closeLatch.countDown();
    }

    @Override
    protected void onText(String message, Callback callback)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("onText {}", message);
        textMessages.offer(message);
        callback.succeeded();
    }

    @Override
    protected void onBinary(ByteBuffer message, Callback callback)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("onBinary {}", message);
        binaryMessages.offer(message);
        callback.succeeded();
    }
}