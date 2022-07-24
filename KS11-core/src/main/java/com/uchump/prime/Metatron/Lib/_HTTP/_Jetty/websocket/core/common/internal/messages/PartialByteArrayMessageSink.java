package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.lang.invoke.MethodHandle;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;

public class PartialByteArrayMessageSink extends AbstractMessageSink
{
    private static byte[] EMPTY_BUFFER = new byte[0];

    public PartialByteArrayMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);
    }

    @Override
    public void accept(Frame frame, Callback callback)
    {
        try
        {
            if (frame.hasPayload() || frame.isFin())
            {
                byte[] buffer = frame.hasPayload() ? BufferUtil.toArray(frame.getPayload()) : EMPTY_BUFFER;
                methodHandle.invoke(buffer, frame.isFin());
            }

            callback.succeeded();
            session.demand(1);
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
    }
}