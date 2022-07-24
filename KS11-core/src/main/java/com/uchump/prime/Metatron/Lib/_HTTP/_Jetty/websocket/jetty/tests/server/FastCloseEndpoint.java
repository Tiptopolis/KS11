package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;

/**
 * On Connect, close socket
 */
public class FastCloseEndpoint extends AbstractCloseEndpoint
{
    @Override
    public void onWebSocketConnect(Session sess)
    {
        log.debug("onWebSocketConnect({})", sess);
        sess.close(StatusCode.NORMAL, "FastCloseServer");
    }
}