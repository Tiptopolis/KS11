package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.messages;


import java.io.IOException;
import java.io.Reader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.List;
import javax.websocket.CloseReason;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.CloseException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.MessageSink;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.ReaderMessageSink;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketFrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders.RegisteredDecoder;

public class DecodedTextStreamMessageSink<T> extends AbstractDecodedMessageSink.Stream<Decoder.TextStream<T>>
{
    public DecodedTextStreamMessageSink(CoreSession session, MethodHandle methodHandle, List<RegisteredDecoder> decoders)
    {
        super(session, methodHandle, decoders);
    }

    @Override
    MessageSink newMessageSink(CoreSession coreSession) throws Exception
    {
        MethodHandle methodHandle = JavaxWebSocketFrameHandlerFactory.getServerMethodHandleLookup()
            .findVirtual(DecodedTextStreamMessageSink.class, "onStreamStart", MethodType.methodType(void.class, Reader.class))
            .bindTo(this);
        return new ReaderMessageSink(coreSession, methodHandle);
    }

    public void onStreamStart(Reader reader)
    {
        try
        {
            T obj = _decoder.decode(reader);
            invoke(obj);
        }
        catch (DecodeException | IOException e)
        {
            throw new CloseException(CloseReason.CloseCodes.CANNOT_ACCEPT.getCode(), "Unable to decode", e);
        }
    }
}