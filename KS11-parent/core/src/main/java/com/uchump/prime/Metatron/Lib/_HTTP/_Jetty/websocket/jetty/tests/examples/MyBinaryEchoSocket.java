package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples;

import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;

/**
 * Echo BINARY messages
 */
@WebSocket
public class MyBinaryEchoSocket
{
    @OnWebSocketMessage
    public void onWebSocketText(Session session, byte[] buf, int offset, int len)
    {
        // Echo message back, asynchronously
        session.getRemote().sendBytes(ByteBuffer.wrap(buf, offset, len), null);
    }
}