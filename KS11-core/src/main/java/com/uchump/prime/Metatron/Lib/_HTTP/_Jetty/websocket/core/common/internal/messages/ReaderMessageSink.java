package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.lang.invoke.MethodHandle;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;

public class ReaderMessageSink extends DispatchedMessageSink
{
    public ReaderMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);
    }

    @Override
    public MessageReader newSink(Frame frame)
    {
        return new MessageReader(session.getInputBufferSize());
    }
}