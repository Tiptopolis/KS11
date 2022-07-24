package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.messages;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.util.List;
import javax.websocket.CloseReason;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.CloseException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.ByteBufferMessageSink;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.MessageSink;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketFrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders.RegisteredDecoder;

public class DecodedBinaryMessageSink<T> extends AbstractDecodedMessageSink.Basic<Decoder.Binary<T>>
{
    private static final Logger LOG = LoggerFactory.getLogger(DecodedBinaryMessageSink.class);

    public DecodedBinaryMessageSink(CoreSession session, MethodHandle methodHandle, List<RegisteredDecoder> decoders)
    {
        super(session, methodHandle, decoders);
    }

    @Override
    MessageSink newMessageSink(CoreSession coreSession) throws Exception
    {
        MethodHandle methodHandle = JavaxWebSocketFrameHandlerFactory.getServerMethodHandleLookup()
            .findVirtual(DecodedBinaryMessageSink.class, "onWholeMessage", MethodType.methodType(void.class, ByteBuffer.class))
            .bindTo(this);
        return new ByteBufferMessageSink(coreSession, methodHandle);
    }

    public void onWholeMessage(ByteBuffer wholeMessage)
    {
        for (Decoder.Binary<T> decoder : _decoders)
        {
            if (decoder.willDecode(wholeMessage))
            {
                try
                {
                    T obj = decoder.decode(wholeMessage);
                    invoke(obj);
                    return;
                }
                catch (DecodeException e)
                {
                    throw new CloseException(CloseReason.CloseCodes.CANNOT_ACCEPT.getCode(), "Unable to decode", e);
                }
            }
        }

        LOG.warn("Message lost, willDecode() has returned false for all decoders in the decoder list.");
    }
}