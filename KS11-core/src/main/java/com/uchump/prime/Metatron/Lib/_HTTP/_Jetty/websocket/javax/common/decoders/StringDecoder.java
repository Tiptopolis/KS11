package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

/**
 * Default implementation of the {@link javax.websocket.Decoder.Text} Message to {@link String} decoder
 */
public class StringDecoder extends AbstractDecoder implements Decoder.Text<String>
{
    public static final StringDecoder INSTANCE = new StringDecoder();

    @Override
    public String decode(String s) throws DecodeException
    {
        return s;
    }

    @Override
    public boolean willDecode(String s)
    {
        return true;
    }
}