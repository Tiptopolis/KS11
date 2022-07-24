package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.lang.invoke.MethodHandle;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;

public class PartialByteBufferMessageSink extends AbstractMessageSink
{
    public PartialByteBufferMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);
    }

    @Override
    public void accept(Frame frame, Callback callback)
    {
        try
        {
            if (frame.hasPayload() || frame.isFin())
                methodHandle.invoke(frame.getPayload(), frame.isFin());

            callback.succeeded();
            session.demand(1);
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
    }
}