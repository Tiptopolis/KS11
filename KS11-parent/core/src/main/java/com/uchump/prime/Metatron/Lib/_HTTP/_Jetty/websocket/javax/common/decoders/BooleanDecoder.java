package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the {@link javax.websocket.Decoder.Text} Message to {@link Boolean} decoder.
 * <p>
 * Note: delegates to {@link Boolean#parseBoolean(String)} and will only support "true" and "false" as boolean values.
 */
public class BooleanDecoder extends AbstractDecoder implements Decoder.Text<Boolean>
{
    public static final BooleanDecoder INSTANCE = new BooleanDecoder();

    @Override
    public Boolean decode(String s) throws DecodeException
    {
        return Boolean.parseBoolean(s);
    }

    @Override
    public boolean willDecode(String s)
    {
        return (s != null);
    }
}