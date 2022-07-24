package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link Double} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class DoubleEncoder extends AbstractEncoder implements Encoder.Text<Double>
{
    @Override
    public String encode(Double object) throws EncodeException
    {
        if (object == null)
        {
            return null;
        }
        return object.toString();
    }
}