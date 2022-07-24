package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.internal;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.Handshaker;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;

/**
 * Selects between the two Handshaker implementations,
 * RFC6455 (HTTP/1.1 WebSocket Upgrades)
 * and RFC68441 (HTTP/2 WebSocket Upgrades)
 */
public class HandshakerSelector implements Handshaker
{
    private final RFC6455Handshaker rfc6455 = new RFC6455Handshaker();
    private final RFC8441Handshaker rfc8441 = new RFC8441Handshaker();

    @Override
    public boolean upgradeRequest(WebSocketNegotiator negotiator, HttpServletRequest request, HttpServletResponse response, WebSocketComponents components, Configuration.Customizer defaultCustomizer) throws IOException
    {
        // Try HTTP/1.1 WS upgrade, if this fails try an HTTP/2 WS upgrade if no response was committed.
        return rfc6455.upgradeRequest(negotiator, request, response, components, defaultCustomizer) ||
            !response.isCommitted() && rfc8441.upgradeRequest(negotiator, request, response, components, defaultCustomizer);
    }
}