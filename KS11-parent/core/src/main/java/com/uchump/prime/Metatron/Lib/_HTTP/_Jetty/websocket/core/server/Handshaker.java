package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.internal.HandshakerSelector;

public interface Handshaker
{
    static Handshaker newInstance()
    {
        return new HandshakerSelector();
    }

    boolean upgradeRequest(WebSocketNegotiator negotiator, HttpServletRequest request, HttpServletResponse response, WebSocketComponents components, Configuration.Customizer defaultCustomizer) throws IOException;
}