package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.framehandlers;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.MessageHandler;

public class FrameHandlerTracker extends MessageHandler
{
    public CountDownLatch openLatch = new CountDownLatch(1);
    public CountDownLatch closeLatch = new CountDownLatch(1);
    public AtomicReference<CloseStatus> closeDetail = new AtomicReference<>();
    public AtomicReference<Throwable> error = new AtomicReference<>();
    public BlockingQueue<String> messageQueue = new LinkedBlockingDeque<>();
    public BlockingQueue<ByteBuffer> bufferQueue = new LinkedBlockingDeque<>();
    public BlockingQueue<String> events = new LinkedBlockingDeque<>();

    public void addEvent(String format, Object... args)
    {
        events.offer(String.format(format, args));
    }

    @Override
    public void onOpen(CoreSession coreSession, Callback callback)
    {
        super.onOpen(coreSession, Callback.from(callback, () -> openLatch.countDown()));
    }

    @Override
    public void onText(String wholeMessage, Callback callback)
    {
        messageQueue.offer(wholeMessage);
        callback.succeeded();
    }

    @Override
    public void onBinary(ByteBuffer wholeMessage, Callback callback)
    {
        bufferQueue.offer(BufferUtil.copy(wholeMessage));
        callback.succeeded();
    }

    @Override
    public void onError(Throwable cause, Callback callback)
    {
        super.onError(cause, Callback.from(callback, () -> error.compareAndSet(null, cause)));
    }

    @Override
    public void onClosed(CloseStatus closeStatus, Callback callback)
    {
        super.onClosed(closeStatus, Callback.from(callback, () ->
        {
            closeDetail.compareAndSet(null, closeStatus);
            closeLatch.countDown();
        }));
    }
}