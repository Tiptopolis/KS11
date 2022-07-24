package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link Character} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class CharacterEncoder extends AbstractEncoder implements Encoder.Text<Character>
{
    @Override
    public String encode(Character object) throws EncodeException
    {
        if (object == null)
        {
            return null;
        }
        return object.toString();
    }
}