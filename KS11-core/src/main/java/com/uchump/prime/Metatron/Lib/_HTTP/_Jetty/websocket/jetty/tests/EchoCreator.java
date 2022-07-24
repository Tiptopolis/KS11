package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketCreator;

public class EchoCreator implements JettyWebSocketCreator
{
    @Override
    public Object createWebSocket(JettyServerUpgradeRequest req, JettyServerUpgradeResponse resp)
    {
        if (req.hasSubProtocol("echo"))
        {
            resp.setAcceptedSubProtocol("echo");
        }

        return new EchoSocket();
    }
}