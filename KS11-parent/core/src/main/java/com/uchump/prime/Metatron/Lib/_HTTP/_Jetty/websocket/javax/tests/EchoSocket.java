package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.websocket.ClientEndpoint;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/")
@ClientEndpoint
public class EchoSocket extends EventSocket
{
    @Override
    public void onMessage(String message) throws IOException
    {
        super.onMessage(message);
        session.getBasicRemote().sendText(message);
    }

    @Override
    public void onMessage(ByteBuffer message) throws IOException
    {
        super.onMessage(message);
        session.getBasicRemote().sendBinary(message);
    }
}