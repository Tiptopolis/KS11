package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import java.nio.ByteBuffer;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;

public class ByteBufferDecoder extends AbstractDecoder implements Decoder.Binary<ByteBuffer>
{
    public static final ByteBufferDecoder INSTANCE = new ByteBufferDecoder();

    @Override
    public ByteBuffer decode(ByteBuffer bytes) throws DecodeException
    {
        return bytes;
    }

    @Override
    public boolean willDecode(ByteBuffer bytes)
    {
        return true;
    }
}