package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the {@link javax.websocket.Decoder.Text} Message to {@link Double} to decoder
 */
public class DoubleDecoder extends AbstractDecoder implements Decoder.Text<Double>
{
    public static final DoubleDecoder INSTANCE = new DoubleDecoder();

    @Override
    public Double decode(String s) throws DecodeException
    {
        try
        {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException e)
        {
            throw new DecodeException(s, "Unable to parse double", e);
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
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}