package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the {@link javax.websocket.Decoder.Text} Message to {@link Short} decoder
 */
public class ShortDecoder extends AbstractDecoder implements Decoder.Text<Short>
{
    public static final ShortDecoder INSTANCE = new ShortDecoder();

    @Override
    public Short decode(String s) throws DecodeException
    {
        try
        {
            return Short.parseShort(s);
        }
        catch (NumberFormatException e)
        {
            throw new DecodeException(s, "Unable to parse Short", e);
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
            Short.parseShort(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}