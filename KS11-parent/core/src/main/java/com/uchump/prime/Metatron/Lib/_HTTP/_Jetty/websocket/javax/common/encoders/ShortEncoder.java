package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link Short} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class ShortEncoder extends AbstractEncoder implements Encoder.Text<Short>
{
    @Override
    public String encode(Short object) throws EncodeException
    {
        if (object == null)
        {
            return null;
        }
        return object.toString();
    }
}