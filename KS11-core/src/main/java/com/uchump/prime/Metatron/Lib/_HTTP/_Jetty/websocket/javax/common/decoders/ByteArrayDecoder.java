package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;


import java.nio.ByteBuffer;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;

public class ByteArrayDecoder extends AbstractDecoder implements Decoder.Binary<byte[]>
{
    public static final ByteArrayDecoder INSTANCE = new ByteArrayDecoder();

    @Override
    public byte[] decode(ByteBuffer bytes) throws DecodeException
    {
        return BufferUtil.toArray(bytes);
    }

    @Override
    public boolean willDecode(ByteBuffer bytes)
    {
        return true;
    }
}