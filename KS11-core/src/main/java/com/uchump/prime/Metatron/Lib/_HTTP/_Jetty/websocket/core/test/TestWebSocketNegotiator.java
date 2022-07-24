package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;


import java.io.IOException;
import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;


public class TestWebSocketNegotiator extends WebSocketNegotiator.AbstractNegotiator
{
    private final FrameHandler frameHandler;

    public TestWebSocketNegotiator(FrameHandler frameHandler)
    {
        this(frameHandler, null);
    }

    public TestWebSocketNegotiator(FrameHandler frameHandler, Configuration.Customizer customizer)
    {
        super(customizer);
        this.frameHandler = frameHandler;
    }

    @Override
    public FrameHandler negotiate(WebSocketNegotiation negotiation) throws IOException
    {
        List<String> offeredSubprotocols = negotiation.getOfferedSubprotocols();
        if (!offeredSubprotocols.isEmpty())
            negotiation.setSubprotocol(offeredSubprotocols.get(0));

        return frameHandler;
    }
}