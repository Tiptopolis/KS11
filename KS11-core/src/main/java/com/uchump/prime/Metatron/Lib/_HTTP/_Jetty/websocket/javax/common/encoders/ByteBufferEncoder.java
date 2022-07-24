package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import java.nio.ByteBuffer;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ByteBufferEncoder implements Encoder.Binary<ByteBuffer>
{
    @Override
    public void destroy()
    {
        /* do nothing */
    }

    @Override
    public ByteBuffer encode(ByteBuffer object) throws EncodeException
    {
        return object;
    }

    @Override
    public void init(EndpointConfig config)
    {
        /* do nothing */
    }
}