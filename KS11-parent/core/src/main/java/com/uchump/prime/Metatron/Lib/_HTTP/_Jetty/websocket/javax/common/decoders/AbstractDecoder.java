package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public abstract class AbstractDecoder implements Decoder
{
    @Override
    public void destroy()
    {
    }

    @Override
    public void init(EndpointConfig config)
    {
    }
}