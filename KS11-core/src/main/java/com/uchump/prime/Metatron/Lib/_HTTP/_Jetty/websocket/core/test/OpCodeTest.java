package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.ProtocolException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.FrameSequence;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matchers;

import static com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode.*;
import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OpCodeTest
{
    @Test
    public void testIsControl()
    {
        assertThat(OpCode.isControlFrame(OpCode.BINARY), Matchers.is(false));
        assertThat(OpCode.isControlFrame(OpCode.CLOSE), Matchers.is(true));
        assertThat(OpCode.isControlFrame(OpCode.CONTINUATION), Matchers.is(false));
        assertThat(OpCode.isControlFrame(OpCode.PING), Matchers.is(true));
        assertThat(OpCode.isControlFrame(OpCode.PONG), Matchers.is(true));
        assertThat(OpCode.isControlFrame(OpCode.TEXT), Matchers.is(false));
    }

    @Test
    public void testIsData()
    {
        assertThat(OpCode.isDataFrame(BINARY), Matchers.is(true));
        assertThat(OpCode.isDataFrame(CLOSE), Matchers.is(false));
        assertThat(OpCode.isDataFrame(CONTINUATION), Matchers.is(true));
        assertThat(OpCode.isDataFrame(PING), Matchers.is(false));
        assertThat(OpCode.isDataFrame(PONG), Matchers.is(false));
        assertThat(OpCode.isDataFrame(TEXT), Matchers.is(true));
    }

    private void testSequence(byte... opcode)
    {
        FrameSequence sequence = new FrameSequence();
        for (int i = 0; i < opcode.length; i++)
        {
            byte o = (byte)(opcode[i] & 0x0F);
            boolean unfin = (opcode[i] & 0x80) != 0;

            sequence.check(o, !unfin);
        }
    }

    private byte unfin(byte o)
    {
        return (byte)(0x80 | o);
    }

    @Test
    public void testFinSequences()
    {
        testSequence(TEXT, BINARY, PING, PONG, CLOSE);
    }

    @Test
    public void testContinuationSequences()
    {
        testSequence(unfin(TEXT), CONTINUATION, CLOSE);
        testSequence(unfin(TEXT), unfin(CONTINUATION), CONTINUATION, CLOSE);
        testSequence(unfin(TEXT), unfin(CONTINUATION), unfin(CONTINUATION), CONTINUATION, CLOSE);
        testSequence(unfin(BINARY), CONTINUATION, CLOSE);
        testSequence(unfin(BINARY), unfin(CONTINUATION), CONTINUATION, CLOSE);
        testSequence(unfin(BINARY), unfin(CONTINUATION), unfin(CONTINUATION), CONTINUATION, CLOSE);
    }

    @Test
    public void testInterleavedContinuationSequences()
    {
        testSequence(unfin(TEXT), PING, CONTINUATION, CLOSE);
        testSequence(unfin(TEXT), PING, unfin(CONTINUATION), PONG, CONTINUATION, CLOSE);
        testSequence(unfin(TEXT), PING, unfin(CONTINUATION), PONG, unfin(CONTINUATION), PING, CONTINUATION, CLOSE);
        testSequence(unfin(BINARY), PING, CONTINUATION, CLOSE);
        testSequence(unfin(BINARY), PING, unfin(CONTINUATION), PONG, CONTINUATION, CLOSE);
        testSequence(unfin(BINARY), PING, unfin(CONTINUATION), PONG, unfin(CONTINUATION), PING, CONTINUATION, CLOSE);
    }

    @Test
    public void testBadContinuationAlreadyFIN()
    {
        assertThrows(ProtocolException.class, () -> testSequence(TEXT, CONTINUATION, CLOSE));
    }

    @Test
    public void testBadContinuationNoCont()
    {
        assertThrows(ProtocolException.class, () -> testSequence(unfin(TEXT), TEXT));
    }

    @Test
    public void testFramesAfterClose()
    {
        assertThrows(ProtocolException.class, () -> testSequence(TEXT, CLOSE, TEXT));
    }

    @Test
    public void testControlFramesAfterClose()
    {
        assertThrows(ProtocolException.class, () -> testSequence(TEXT, CLOSE, PING));
    }
}