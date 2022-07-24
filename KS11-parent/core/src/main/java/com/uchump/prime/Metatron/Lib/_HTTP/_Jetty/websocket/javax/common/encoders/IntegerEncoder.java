package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link Integer} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class IntegerEncoder extends AbstractEncoder implements Encoder.Text<Integer>
{
    @Override
    public String encode(Integer object) throws EncodeException
    {
        if (object == null)
        {
            return null;
        }
        return object.toString();
    }
}