package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * Default encoder for {@link String} to {@link javax.websocket.Encoder.Text} Message encoder
 */
public class StringEncoder extends AbstractEncoder implements Encoder.Text<String>
{
    @Override
    public String encode(String object) throws EncodeException
    {
        return object;
    }
}