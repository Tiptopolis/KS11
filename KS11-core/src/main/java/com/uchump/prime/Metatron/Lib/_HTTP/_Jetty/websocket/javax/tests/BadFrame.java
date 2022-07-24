package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;

/**
 * Allow Fuzzer / Generator to create bad frames for testing frame validation
 */
public class BadFrame extends Frame
{
    public BadFrame(byte opcode)
    {
        super(OpCode.CONTINUATION);
        super.finRsvOp = (byte)((finRsvOp & 0xF0) | (opcode & 0x0F));
        // NOTE: Not setting Frame.Type intentionally
    }

    @Override
    public boolean isControlFrame()
    {
        return false;
    }

    @Override
    public boolean isDataFrame()
    {
        return false;
    }
}