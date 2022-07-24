package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.messages;


import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.List;
import javax.websocket.CloseReason;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.CloseException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.InputStreamMessageSink;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.MessageSink;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketFrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders.RegisteredDecoder;

public class DecodedBinaryStreamMessageSink<T> extends AbstractDecodedMessageSink.Stream<Decoder.BinaryStream<T>>
{
    public DecodedBinaryStreamMessageSink(CoreSession session, MethodHandle methodHandle, List<RegisteredDecoder> decoders)
    {
        super(session, methodHandle, decoders);
    }

    @Override
    MessageSink newMessageSink(CoreSession coreSession) throws Exception
    {
        MethodHandle methodHandle = JavaxWebSocketFrameHandlerFactory.getServerMethodHandleLookup()
            .findVirtual(DecodedBinaryStreamMessageSink.class, "onStreamStart", MethodType.methodType(void.class, InputStream.class))
            .bindTo(this);
        return new InputStreamMessageSink(coreSession, methodHandle);
    }

    public void onStreamStart(InputStream stream)
    {
        try
        {
            T obj = _decoder.decode(stream);
            invoke(obj);
        }
        catch (DecodeException | IOException e)
        {
            throw new CloseException(CloseReason.CloseCodes.CANNOT_ACCEPT.getCode(), "Unable to decode", e);
        }
    }
}