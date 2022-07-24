package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the {@link javax.websocket.Decoder.Text} Message to {@link Integer} decoder
 */
public class IntegerDecoder extends AbstractDecoder implements Decoder.Text<Integer>
{
    public static final IntegerDecoder INSTANCE = new IntegerDecoder();

    @Override
    public Integer decode(String s) throws DecodeException
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            throw new DecodeException(s, "Unable to parse Integer", e);
        }
    }

    @Override
    public boolean willDecode(String s)
    {
        if (s == null)
        {
            return false;
        }

        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}