package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Behavior;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.ExtensionStack;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Negotiated;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Parser;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.FrameValidation;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

public class ParserCapture
{
    private final Parser parser;
    private final WebSocketCoreSession coreSession;
    public BlockingQueue<Frame> framesQueue = new LinkedBlockingDeque<>();
    public boolean closed = false;
    public boolean copy;

    public ParserCapture()
    {
        this(true);
    }

    public ParserCapture(boolean copy)
    {
        this(copy, Behavior.CLIENT);
    }

    public ParserCapture(boolean copy, Behavior behavior)
    {
        this.copy = copy;

        WebSocketComponents components = new WebSocketComponents();
        ExtensionStack exStack = new ExtensionStack(components, Behavior.SERVER);
        exStack.negotiate(new LinkedList<>(), new LinkedList<>());
        this.coreSession = new WebSocketCoreSession(new TestMessageHandler(), behavior, Negotiated.from(exStack), components);
        coreSession.setAutoFragment(false);
        coreSession.setMaxFrameSize(0);
        this.parser = new Parser(components.getBufferPool(), coreSession);
    }

    public void parse(ByteBuffer buffer)
    {
        while (buffer.hasRemaining())
        {
            Frame frame = parser.parse(buffer);
            if (frame == null)
                break;

            FrameValidation.assertValidIncoming(frame, coreSession);

            if (!onFrame(frame))
                break;
        }
    }

    public void assertHasFrame(byte opCode, int expectedCount)
    {
        int count = 0;
        for (Frame frame : framesQueue)
        {
            if (frame.getOpCode() == opCode)
                count++;
        }
        assertThat("Frames[op=" + opCode + "].count", count, is(expectedCount));
    }

    public boolean onFrame(Frame frame)
    {
        framesQueue.offer(copy ? Frame.copy(frame) : frame);
        if (frame.getOpCode() == OpCode.CLOSE)
            closed = true;
        return true; // it is consumed
    }

    public Parser getParser()
    {
        return parser;
    }

    public WebSocketCoreSession getCoreSession()
    {
        return coreSession;
    }
}