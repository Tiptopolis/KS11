package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;
import java.nio.ByteBuffer;
import javax.websocket.PongMessage;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;

public class JavaxWebSocketPongMessage implements PongMessage
{
    private final ByteBuffer data;

    public JavaxWebSocketPongMessage(ByteBuffer buf)
    {
        this.data = buf;
    }

    @Override
    public ByteBuffer getApplicationData()
    {
        if (data == null)
        {
            return BufferUtil.EMPTY_BUFFER;
        }
        return data.slice();
    }
}