package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.internal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;

public class MockEndpoint implements EndPoint
{
    public static final String NOT_SUPPORTED = "Not supported by MockEndPoint";

    @Override
    public InetSocketAddress getLocalAddress()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public SocketAddress getLocalSocketAddress()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public InetSocketAddress getRemoteAddress()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public SocketAddress getRemoteSocketAddress()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public boolean isOpen()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public long getCreatedTimeStamp()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void shutdownOutput()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public boolean isOutputShutdown()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public boolean isInputShutdown()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void close()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void close(Throwable cause)
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public int fill(ByteBuffer buffer) throws IOException
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public boolean flush(ByteBuffer... buffer) throws IOException
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public Object getTransport()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public long getIdleTimeout()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void setIdleTimeout(long idleTimeout)
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void fillInterested(Callback callback) throws ReadPendingException
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public boolean tryFillInterested(Callback callback)
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public boolean isFillInterested()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void write(Callback callback, ByteBuffer... buffers) throws WritePendingException
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public Connection getConnection()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void setConnection(Connection connection)
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void onOpen()
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void onClose(Throwable cause)
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void upgrade(Connection newConnection)
    {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }
}