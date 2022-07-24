package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples;

import java.io.IOException;
import java.security.Principal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketCreator;

public class MyAuthedCreator implements JettyWebSocketCreator
{
    @Override
    public Object createWebSocket(JettyServerUpgradeRequest req, JettyServerUpgradeResponse resp)
    {
        try
        {
            // Is Authenticated?
            Principal principal = req.getUserPrincipal();
            if (principal == null)
            {
                resp.sendForbidden("Not authenticated yet");
                return null;
            }

            // Is Authorized?
            if (!req.isUserInRole("websocket"))
            {
                resp.sendForbidden("Not authenticated yet");
                return null;
            }

            // Return websocket
            return new MyEchoSocket();
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
        }
        // no websocket
        return null;
    }
}