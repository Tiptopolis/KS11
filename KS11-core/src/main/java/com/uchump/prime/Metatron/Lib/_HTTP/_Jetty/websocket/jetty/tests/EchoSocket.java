package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;


@SuppressWarnings("unused")
@WebSocket
public class EchoSocket extends EventSocket
{
    @Override
    public void onMessage(String message) throws IOException
    {
        super.onMessage(message);
        session.getRemote().sendString(message);
    }

    @Override
    public void onMessage(byte[] buf, int offset, int len) throws IOException
    {
        super.onMessage(buf, offset, len);
        session.getRemote().sendBytes(ByteBuffer.wrap(buf, offset, len));
    }
}