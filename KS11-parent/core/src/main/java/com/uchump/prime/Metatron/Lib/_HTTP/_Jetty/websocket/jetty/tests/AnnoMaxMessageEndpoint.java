package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.io.IOException;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;


@SuppressWarnings("unused")
@WebSocket(maxTextMessageSize = 100 * 1024)
public class AnnoMaxMessageEndpoint
{
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException
    {
        session.getRemote().sendString(msg);
    }
}