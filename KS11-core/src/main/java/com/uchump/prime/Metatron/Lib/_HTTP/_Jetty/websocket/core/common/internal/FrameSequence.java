package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.AutoLock;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.ProtocolException;

public class FrameSequence
{
    private final AutoLock lock = new AutoLock();
    private byte state = OpCode.UNDEFINED;

    public void check(byte opcode, boolean fin) throws ProtocolException
    {
        try (AutoLock l = lock.lock())
        {
            if (state == OpCode.CLOSE)
                throw new ProtocolException(OpCode.name(opcode) + " after CLOSE");

            switch (opcode)
            {
                case OpCode.UNDEFINED:
                    throw new ProtocolException("UNDEFINED OpCode: " + OpCode.name(opcode));

                case OpCode.CONTINUATION:
                    if (state == OpCode.UNDEFINED)
                        throw new ProtocolException("CONTINUATION after fin==true");
                    if (fin)
                        state = OpCode.UNDEFINED;
                    break;

                case OpCode.CLOSE:
                    state = OpCode.CLOSE;
                    break;

                case OpCode.PING:
                case OpCode.PONG:
                    break;

                case OpCode.TEXT:
                case OpCode.BINARY:
                default:
                    if (state != OpCode.UNDEFINED)
                        throw new ProtocolException("DataFrame before fin==true");
                    if (!fin)
                        state = opcode;
                    break;
            }
        }
    }
}