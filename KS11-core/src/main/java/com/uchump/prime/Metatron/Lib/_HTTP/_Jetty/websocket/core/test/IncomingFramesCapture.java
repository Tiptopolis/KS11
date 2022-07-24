package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.IncomingFrames;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

public class IncomingFramesCapture implements IncomingFrames
{
    public BlockingQueue<Frame> frames = new LinkedBlockingDeque<>();

    @Override
    public void onFrame(Frame frame, Callback callback)
    {
        Frame copy = Frame.copy(frame);
        frames.offer(copy);
        callback.succeeded();
    }

    public void assertHasOpCount(byte opCode, int expectedCount)
    {
        assertThat("Frame Count [op=" + opCode + "]", getFrameCount(opCode), is(expectedCount));
    }

    public void assertFrameCount(int expectedCount)
    {
        assertThat("Frame Count", frames.size(), is(expectedCount));
    }

    public int getFrameCount(byte op)
    {
        int count = 0;
        for (Frame frame : frames)
        {
            if (frame.getOpCode() == op)
            {
                count++;
            }
        }
        return count;
    }
}