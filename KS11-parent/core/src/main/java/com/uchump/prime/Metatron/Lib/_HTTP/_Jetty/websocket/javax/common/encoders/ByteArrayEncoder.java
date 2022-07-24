package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import java.nio.ByteBuffer;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ByteArrayEncoder implements Encoder.Binary<byte[]>
{
    @Override
    public void destroy()
    {
        /* do nothing */
    }

    @Override
    public ByteBuffer encode(byte[] object) throws EncodeException
    {
        return ByteBuffer.wrap(object);
    }

    @Override
    public void init(EndpointConfig config)
    {
        /* do nothing */
    }
}