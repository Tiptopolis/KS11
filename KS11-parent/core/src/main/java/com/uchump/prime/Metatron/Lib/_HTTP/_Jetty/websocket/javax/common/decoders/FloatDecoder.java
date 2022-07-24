package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the Text Message to {@link Float} decoder
 */
public class FloatDecoder extends AbstractDecoder implements Decoder.Text<Float>
{
    public static final FloatDecoder INSTANCE = new FloatDecoder();

    @Override
    public Float decode(String s) throws DecodeException
    {
        try
        {
            Float val = Float.parseFloat(s);
            if (val.isNaN())
            {
                throw new DecodeException(s, "NaN");
            }
            return val;
        }
        catch (NumberFormatException e)
        {
            throw new DecodeException(s, "Unable to parse float", e);
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
            Float val = Float.parseFloat(s);
            return (!val.isNaN());
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}