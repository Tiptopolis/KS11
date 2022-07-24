package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link Float} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class FloatEncoder extends AbstractEncoder implements Encoder.Text<Float>
{
    @Override
    public String encode(Float object) throws EncodeException
    {
        if (object == null)
        {
            return null;
        }
        return object.toString();
    }
}