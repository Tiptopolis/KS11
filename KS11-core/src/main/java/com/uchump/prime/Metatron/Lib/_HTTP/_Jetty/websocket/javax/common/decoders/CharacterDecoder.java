package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the {@link javax.websocket.Decoder.Text} Message to {@link Character} decoder
 */
public class CharacterDecoder extends AbstractDecoder implements Decoder.Text<Character>
{
    public static final CharacterDecoder INSTANCE = new CharacterDecoder();

    @Override
    public Character decode(String s) throws DecodeException
    {
        return s.charAt(0);
    }

    @Override
    public boolean willDecode(String s)
    {
        if (s == null)
        {
            return false;
        }
        if (s.length() == 1)
        {
            return true;
        }
        // can only parse 1 character
        return false;
    }
}