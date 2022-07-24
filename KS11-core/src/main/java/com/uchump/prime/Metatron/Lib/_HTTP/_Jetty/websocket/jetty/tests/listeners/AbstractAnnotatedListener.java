package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.listeners;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketConnect;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketError;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class AbstractAnnotatedListener
{
    protected Session _session;

    @OnWebSocketConnect
    public void onWebSocketConnect(Session session)
    {
        _session = session;
    }

    @OnWebSocketError
    public void onWebSocketError(Throwable thr)
    {
        thr.printStackTrace();
    }

    public void sendText(String message, boolean last)
    {
        try
        {
            _session.getRemote().sendPartialString(message, last);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendBinary(ByteBuffer message, boolean last)
    {
        try
        {
            _session.getRemote().sendPartialBytes(message, last);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}