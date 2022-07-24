package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the {@link javax.websocket.Decoder.Text} Message to {@link Byte} decoder
 */
public class ByteDecoder extends AbstractDecoder implements Decoder.Text<Byte>
{
    public static final ByteDecoder INSTANCE = new ByteDecoder();

    @Override
    public Byte decode(String s) throws DecodeException
    {
        try
        {
            return Byte.parseByte(s);
        }
        catch (NumberFormatException e)
        {
            throw new DecodeException(s, "Unable to parse Byte", e);
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
            Byte.parseByte(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}