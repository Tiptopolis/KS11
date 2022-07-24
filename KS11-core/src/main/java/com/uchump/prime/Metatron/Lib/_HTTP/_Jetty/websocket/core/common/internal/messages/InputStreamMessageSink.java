package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.lang.invoke.MethodHandle;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;

public class InputStreamMessageSink extends DispatchedMessageSink
{
    public InputStreamMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);
    }

    @Override
    public MessageSink newSink(Frame frame)
    {
        return new MessageInputStream();
    }
}