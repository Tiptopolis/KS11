package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OutgoingFrames;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

public class OutgoingFramesCapture implements OutgoingFrames
{
    public BlockingQueue<Frame> frames = new LinkedBlockingDeque<>();

    public void assertFrameCount(int expectedCount)
    {
        assertThat("Frame Count", frames.size(), is(expectedCount));
    }

    public void assertHasOpCount(byte opCode, int expectedCount)
    {
        assertThat("Frame Count [op=" + opCode + "]", getFrameCount(opCode), is(expectedCount));
    }

    public void dump()
    {
        System.out.printf("Captured %d outgoing writes%n", frames.size());
        int i = 0;
        for (Frame frame : frames)
        {
            System.out.printf("[%3d] %s%n", i++, frame);
            System.out.printf("      %s%n", BufferUtil.toDetailString(frame.getPayload()));
        }
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

    @Override
    public void sendFrame(Frame frame, Callback callback, boolean batch)
    {
        frames.add(Frame.copy(frame));
        // Consume bytes
        ByteBuffer payload = frame.getPayload();
        payload.position(payload.limit());
        // notify callback
        if (callback != null)
        {
            callback.succeeded();
        }
    }
}