package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders;

import java.io.IOException;
import java.io.Reader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class ReaderDecoder implements Decoder.TextStream<Reader>
{
    @Override
    public Reader decode(Reader reader) throws DecodeException, IOException
    {
        return reader;
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