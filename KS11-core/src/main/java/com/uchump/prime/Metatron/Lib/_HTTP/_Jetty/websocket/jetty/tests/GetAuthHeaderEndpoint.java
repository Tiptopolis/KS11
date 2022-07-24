package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;
import java.io.IOException;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketConnect;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;




@SuppressWarnings("unused")
@WebSocket
public class GetAuthHeaderEndpoint
{
    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException
    {
        String authHeaderName = "Authorization";
        String authHeaderValue = session.getUpgradeRequest().getHeader(authHeaderName);
        session.getRemote().sendString("Header[" + authHeaderName + "]=" + authHeaderValue);
    }
}