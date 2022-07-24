package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;

public class CloseInOnCloseEndpoint extends AbstractCloseEndpoint
{
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        getSession().close(StatusCode.SERVER_ERROR, "this should be a noop");
        super.onWebSocketClose(statusCode, reason);
    }
}