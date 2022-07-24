package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common;

import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;

public class JettyWebSocketFrame implements com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Frame
{
    private final Frame frame;

    public JettyWebSocketFrame(Frame frame)
    {
        this.frame = frame;
    }

    @Override
    public byte[] getMask()
    {
        return frame.getMask();
    }

    @Override
    public byte getOpCode()
    {
        return frame.getOpCode();
    }

    @Override
    public ByteBuffer getPayload()
    {
        return frame.getPayload().asReadOnlyBuffer();
    }

    @Override
    public int getPayloadLength()
    {
        return frame.getPayloadLength();
    }

    @Override
    public Type getType()
    {
        return Type.from(getOpCode());
    }

    @Override
    public boolean hasPayload()
    {
        return frame.hasPayload();
    }

    @Override
    public boolean isFin()
    {
        return frame.isFin();
    }

    @Override
    public boolean isMasked()
    {
        return frame.isMasked();
    }

    @Override
    public boolean isRsv1()
    {
        return frame.isRsv1();
    }

    @Override
    public boolean isRsv2()
    {
        return frame.isRsv2();
    }

    @Override
    public boolean isRsv3()
    {
        return frame.isRsv3();
    }

    @Override
    public String toString()
    {
        return frame.toString();
    }
}