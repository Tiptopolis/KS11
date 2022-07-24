package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import java.io.IOException;
import java.io.InputStream;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class InputStreamDecoder implements Decoder.BinaryStream<InputStream>
{
    @Override
    public InputStream decode(InputStream is) throws DecodeException, IOException
    {
        return is;
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void init(EndpointConfig config)
    {
    }
}