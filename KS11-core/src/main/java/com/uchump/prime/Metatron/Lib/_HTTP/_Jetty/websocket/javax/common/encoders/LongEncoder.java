package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link Long} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class LongEncoder extends AbstractEncoder implements Encoder.Text<Long>
{
    @Override
    public String encode(Long object) throws EncodeException
    {
        if (object == null)
        {
            return null;
        }
        return object.toString();
    }
}