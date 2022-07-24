package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;


import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Parser;

public class ParserCapture
{
    private final Parser parser;
    public BlockingQueue<Frame> framesQueue = new LinkedBlockingDeque<>();
    public boolean closed = false;

    public ParserCapture(Parser parser)
    {
        this.parser = parser;
    }

    public void parse(ByteBuffer buffer)
    {
        while (buffer.hasRemaining())
        {
            Frame frame = parser.parse(buffer);
            if (frame == null)
                break;
            if (!onFrame(frame))
                break;
        }
    }

    public boolean onFrame(Frame frame)
    {
        framesQueue.offer(Frame.copy(frame));
        if (frame.getOpCode() == OpCode.CLOSE)
            closed = true;
        return true; // it is consumed
    }
}