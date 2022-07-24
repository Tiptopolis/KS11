package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples;

import javax.servlet.annotation.WebServlet;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;

/**
 * Example WebSocket, simple echo
 */
@WebServlet
public class MyEchoSocket
{
    @OnWebSocketMessage
    public void onWebSocketText(Session session, String message)
    {
        // Echo message back, asynchronously
        session.getRemote().sendString(message, null);
    }
}