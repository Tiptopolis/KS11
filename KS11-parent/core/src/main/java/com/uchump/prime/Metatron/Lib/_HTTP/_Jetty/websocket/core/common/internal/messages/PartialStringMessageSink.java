package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.lang.invoke.MethodHandle;
import java.util.Objects;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Utf8StringBuilder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;


public class PartialStringMessageSink extends AbstractMessageSink
{
    private Utf8StringBuilder out;

    public PartialStringMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);
        Objects.requireNonNull(methodHandle, "MethodHandle");
    }

    @Override
    public void accept(Frame frame, Callback callback)
    {
        try
        {
            if (out == null)
                out = new Utf8StringBuilder(session.getInputBufferSize());

            out.append(frame.getPayload());
            if (frame.isFin())
            {
                methodHandle.invoke(out.toString(), true);
                out = null;
            }
            else
            {
                methodHandle.invoke(out.takePartialString(), false);
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