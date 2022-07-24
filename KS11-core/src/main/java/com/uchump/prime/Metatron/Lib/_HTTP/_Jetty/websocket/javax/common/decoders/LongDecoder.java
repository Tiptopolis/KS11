package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the Text Message to {@link Long} decoder
 */
public class LongDecoder extends AbstractDecoder implements Decoder.Text<Long>
{
    public static final LongDecoder INSTANCE = new LongDecoder();

    @Override
    public Long decode(String s) throws DecodeException
    {
        try
        {
            return Long.parseLong(s);
        }
        catch (NumberFormatException e)
        {
            throw new DecodeException(s, "Unable to parse Long", e);
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
            Long.parseLong(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}