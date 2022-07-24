package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;

/**
 * On Connect, throw unhandled exception
 */
public class FastFailEndpoint extends AbstractCloseEndpoint
{
    @Override
    public void onWebSocketConnect(Session sess)
    {
        log.debug("onWebSocketConnect({})", sess);
        // Test failure due to unhandled exception
        // this should trigger a fast-fail closure during open/connect
        throw new RuntimeException("Intentional FastFail");
    }
}