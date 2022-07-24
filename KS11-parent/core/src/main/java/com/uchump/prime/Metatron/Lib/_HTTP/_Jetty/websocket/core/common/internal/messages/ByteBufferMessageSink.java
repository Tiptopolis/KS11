package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.util.Objects;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferCallbackAccumulator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.InvalidSignatureException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.MessageTooLargeException;

public class ByteBufferMessageSink extends AbstractMessageSink
{
    private ByteBufferCallbackAccumulator out;

    public ByteBufferMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);

        // Validate onMessageMethod
        Objects.requireNonNull(methodHandle, "MethodHandle");
        MethodType onMessageType = MethodType.methodType(Void.TYPE, ByteBuffer.class);
        if (methodHandle.type() != onMessageType)
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
                throw new MessageTooLargeException(String.format("Binary message too large: (actual) %,d > (configured max binary message size) %,d",
                    size, maxBinaryMessageSize));
            }

            // If we are fin and no OutputStream has been created we don't need to aggregate.
            if (frame.isFin() && (out == null))
            {
                if (frame.hasPayload())
                    methodHandle.invoke(frame.getPayload());
                else
                    methodHandle.invoke(BufferUtil.EMPTY_BUFFER);

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
                ByteBufferPool bufferPool = session.getByteBufferPool();
                ByteBuffer buffer = bufferPool.acquire(out.getLength(), false);
                out.writeTo(buffer);

                try
                {
                    methodHandle.invoke(buffer);
                }
                finally
                {
                    bufferPool.release(buffer);
                }
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
                out = null;
            }
        }
    }
}