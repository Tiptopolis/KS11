package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link Byte} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class ByteEncoder extends AbstractEncoder implements Encoder.Text<Byte>
{
    @Override
    public String encode(Byte object) throws EncodeException
    {
        if (object == null)
        {
            return null;
        }
        return object.toString();
    }
}