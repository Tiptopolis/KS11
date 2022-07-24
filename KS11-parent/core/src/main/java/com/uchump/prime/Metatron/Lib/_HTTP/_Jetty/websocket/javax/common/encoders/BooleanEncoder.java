package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link Boolean} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class BooleanEncoder extends AbstractEncoder implements Encoder.Text<Boolean>
{
    @Override
    public String encode(Boolean object) throws EncodeException
    {
        if (object == null)
        {
            return null;
        }
        return object.toString();
    }
}