package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;

public class FrameEntry
{
    public final Frame frame;
    public final Callback callback;
    public final boolean batch;

    public FrameEntry(Frame frame, Callback callback, boolean batch)
    {
        this.frame = frame;
        this.callback = callback;
        this.batch = batch;
    }

    @Override
    public String toString()
    {
        return frame.toString();
    }
}