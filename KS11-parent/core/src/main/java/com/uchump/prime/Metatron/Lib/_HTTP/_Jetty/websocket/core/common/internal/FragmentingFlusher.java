package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;

/**
 * Used to split large data frames into multiple frames below the maxFrameSize.
 * Control frames and dataFrames smaller than the maxFrameSize will be forwarded
 * directly to {@link #forwardFrame(Frame, Callback, boolean)}.
 */
public abstract class FragmentingFlusher extends TransformingFlusher
{
    private static final Logger LOG = LoggerFactory.getLogger(FragmentingFlusher.class);
    private final Configuration configuration;
    private FrameEntry current;
    private ByteBuffer payload;

    public FragmentingFlusher(Configuration configuration)
    {
        this.configuration = configuration;
    }

    abstract void forwardFrame(Frame frame, Callback callback, boolean batch);

    @Override
    protected boolean onFrame(Frame frame, Callback callback, boolean batch)
    {
        long maxFrameSize = configuration.getMaxFrameSize();
        if (frame.isControlFrame() || maxFrameSize <= 0 || frame.getPayloadLength() <= maxFrameSize)
        {
            forwardFrame(frame, callback, batch);
            return true;
        }

        current = new FrameEntry(frame, callback, batch);
        payload = frame.getPayload().slice();

        boolean finished = fragment(callback, true);
        if (finished)
        {
            current = null;
            payload = null;
        }
        return finished;
    }

    @Override
    protected boolean transform(Callback callback)
    {
        boolean finished = fragment(callback, false);
        if (finished)
        {
            current = null;
            payload = null;
        }
        return finished;
    }

    private boolean fragment(Callback callback, boolean first)
    {
        Frame frame = current.frame;
        int remaining = payload.remaining();
        long maxFrameSize = configuration.getMaxFrameSize();
        int fragmentSize = (int)Math.min(remaining, maxFrameSize);

        boolean continuation = (frame.getOpCode() == OpCode.CONTINUATION) || !first;
        Frame fragment = new Frame(continuation ? OpCode.CONTINUATION : frame.getOpCode());
        boolean finished = (maxFrameSize <= 0 || remaining <= maxFrameSize);
        fragment.setFin(frame.isFin() && finished);

        // If we don't need to fragment just forward with original payload.
        if (finished)
        {
            fragment.setPayload(payload);
            forwardFrame(fragment, callback, current.batch);
            return true;
        }

        // Slice the fragmented payload from the buffer.
        int limit = payload.limit();
        int newLimit = payload.position() + fragmentSize;
        payload.limit(newLimit);
        ByteBuffer payloadFragment = payload.slice();
        payload.limit(limit);
        fragment.setPayload(payloadFragment);
        payload.position(newLimit);
        if (LOG.isDebugEnabled())
            LOG.debug("Fragmented {}->{}", frame, fragment);

        forwardFrame(fragment, callback, current.batch);
        return false;
    }
}