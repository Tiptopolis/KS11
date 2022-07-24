package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BlockingArrayQueue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;


public class TestFrameHandler implements SynchronousFrameHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(TestFrameHandler.class);

    protected CoreSession coreSession;
    public BlockingQueue<Frame> receivedFrames = new BlockingArrayQueue<>();
    public CloseStatus closeStatus;
    public Throwable failure;

    public CountDownLatch open = new CountDownLatch(1);
    public CountDownLatch error = new CountDownLatch(1);
    public CountDownLatch closed = new CountDownLatch(1);

    public CoreSession getCoreSession()
    {
        return coreSession;
    }

    public BlockingQueue<Frame> getFrames()
    {
        return receivedFrames;
    }

    public Throwable getError()
    {
        return failure;
    }

    @Override
    public void onOpen(CoreSession coreSession)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("onOpen {}", coreSession);
        this.coreSession = coreSession;
        open.countDown();
    }

    @Override
    public void onFrame(Frame frame)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("onFrame: " + OpCode.name(frame.getOpCode()) + ":" + BufferUtil.toDetailString(frame.getPayload()));
        receivedFrames.offer(Frame.copy(frame));
    }

    @Override
    public void onClosed(CloseStatus closeStatus)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("onClosed {}", closeStatus);
        this.closeStatus = closeStatus;
        closed.countDown();
    }

    @Override
    public void onError(Throwable cause)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("onError ", cause);
        failure = cause;
        error.countDown();
    }

    public void sendText(String text)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("sendText {} ", text);
        Frame frame = new Frame(OpCode.TEXT, text);
        getCoreSession().sendFrame(frame, Callback.NOOP, false);
    }

    public void sendFrame(Frame frame)
    {
        sendFrame(frame, Callback.NOOP, false);
    }

    public void sendFrame(Frame frame, Callback callback, boolean batch)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("sendFrame {} ", frame);
        getCoreSession().sendFrame(frame, callback, batch);
    }

    public void sendClose()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("sendClose");
        Frame frame = new Frame(OpCode.CLOSE);
        getCoreSession().sendFrame(frame, Callback.NOOP, false);
    }
}