package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketCreator;

public class MyAdvancedEchoCreator implements JettyWebSocketCreator
{
    private MyBinaryEchoSocket binaryEcho;
    private MyEchoSocket textEcho;

    public MyAdvancedEchoCreator()
    {
        // Create the reusable sockets
        this.binaryEcho = new MyBinaryEchoSocket();
        this.textEcho = new MyEchoSocket();
    }

    @Override
    public Object createWebSocket(JettyServerUpgradeRequest req, JettyServerUpgradeResponse resp)
    {
        for (String subprotocol : req.getSubProtocols())
        {
            if ("binary".equals(subprotocol))
            {
                resp.setAcceptedSubProtocol(subprotocol);
                return binaryEcho;
            }
            if ("text".equals(subprotocol))
            {
                resp.setAcceptedSubProtocol(subprotocol);
                return textEcho;
            }
        }

        // No valid subprotocol in request, ignore the request
        return null;
    }
}