package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.lang.invoke.MethodHandle;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Utf8StringBuilder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.MessageTooLargeException;

public class StringMessageSink extends AbstractMessageSink
{
    private Utf8StringBuilder out;
    private int size;

    public StringMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);
        this.size = 0;
    }

    @Override
    public void accept(Frame frame, Callback callback)
    {
        try
        {
            size += frame.getPayloadLength();
            long maxTextMessageSize = session.getMaxTextMessageSize();
            if (maxTextMessageSize > 0 && size > maxTextMessageSize)
            {
                throw new MessageTooLargeException(String.format("Text message too large: (actual) %,d > (configured max text message size) %,d",
                    size, maxTextMessageSize));
            }

            if (out == null)
                out = new Utf8StringBuilder(session.getInputBufferSize());

            out.append(frame.getPayload());
            if (frame.isFin())
                methodHandle.invoke(out.toString());

            callback.succeeded();
            session.demand(1);
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
        finally
        {
            if (frame.isFin())
            {
                // reset
                size = 0;
                out = null;
            }
        }
    }
}