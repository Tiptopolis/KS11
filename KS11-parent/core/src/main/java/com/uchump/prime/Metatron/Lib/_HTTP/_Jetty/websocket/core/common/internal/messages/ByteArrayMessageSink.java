package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferCallbackAccumulator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.InvalidSignatureException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.MessageTooLargeException;

public class ByteArrayMessageSink extends AbstractMessageSink
{
    private static final byte[] EMPTY_BUFFER = new byte[0];
    private ByteBufferCallbackAccumulator out;

    public ByteArrayMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);

        // This uses the offset length byte array signature not supported by javax websocket.
        // The javax layer instead uses decoders for whole byte array messages instead of this message sink.
        MethodType onMessageType = MethodType.methodType(Void.TYPE, byte[].class, int.class, int.class);
        if (methodHandle.type().changeReturnType(void.class) != onMessageType.changeReturnType(void.class))
        {
            throw InvalidSignatureException.build(onMessageType, methodHandle.type());
        }
    }

    @Override
    public void accept(Frame frame, Callback callback)
    {
        try
        {
            long size = (out == null ? 0 : out.getLength()) + frame.getPayloadLength();
            long maxBinaryMessageSize = session.getMaxBinaryMessageSize();
            if (maxBinaryMessageSize > 0 && size > maxBinaryMessageSize)
            {
                throw new MessageTooLargeException(
                    String.format("Binary message too large: (actual) %,d > (configured max binary message size) %,d", size, maxBinaryMessageSize));
            }

            // If we are fin and no OutputStream has been created we don't need to aggregate.
            if (frame.isFin() && (out == null))
            {
                if (frame.hasPayload())
                {
                    byte[] buf = BufferUtil.toArray(frame.getPayload());
                    methodHandle.invoke(buf, 0, buf.length);
                }
                else
                    methodHandle.invoke(EMPTY_BUFFER, 0, 0);

                callback.succeeded();
                session.demand(1);
                return;
            }

            // Aggregate the frame payload.
            if (frame.hasPayload())
            {
                ByteBuffer payload = frame.getPayload();
                if (out == null)
                    out = new ByteBufferCallbackAccumulator();
                out.addEntry(payload, callback);
            }

            // If the methodHandle throws we don't want to fail callback twice.
            callback = Callback.NOOP;
            if (frame.isFin())
            {
                byte[] buf = out.takeByteArray();
                methodHandle.invoke(buf, 0, buf.length);
            }

            session.demand(1);
        }
        catch (Throwable t)
        {
            if (out != null)
                out.fail(t);
            callback.failed(t);
        }
        finally
        {
            if (frame.isFin())
            {
                // reset
                out = null;
            }
        }
    }
}