package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.stream.Stream;


import org.junit.jupiter.params.provider.Arguments;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IO;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPartialListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;

public class BinaryListeners
{
    public static Stream<Arguments> getBinaryListeners()
    {
        return Stream.of(
            OffsetByteArrayWholeListener.class,
            OffsetByteBufferPartialListener.class,
            AnnotatedByteBufferWholeListener.class,
            AnnotatedByteArrayWholeListener.class,
            AnnotatedOffsetByteArrayWholeListener.class,
            AnnotatedInputStreamWholeListener.class,
            AnnotatedReverseArgumentPartialListener.class
        ).map(Arguments::of);
    }

    public static class OffsetByteArrayWholeListener extends AbstractListener implements WebSocketListener
    {
        @Override
        public void onWebSocketBinary(byte[] payload, int offset, int len)
        {
            sendBinary(BufferUtil.toBuffer(payload, offset, len), true);
        }
    }

    public static class OffsetByteBufferPartialListener extends AbstractListener implements WebSocketPartialListener
    {
        @Override
        public void onWebSocketPartialBinary(ByteBuffer payload, boolean fin)
        {
            sendBinary(payload, fin);
        }
    }

    @WebSocket
    public static class AnnotatedByteBufferWholeListener extends AbstractAnnotatedListener
    {
        @OnWebSocketMessage
        public void onMessage(ByteBuffer message)
        {
            sendBinary(message, true);
        }
    }

    @WebSocket
    public static class AnnotatedByteArrayWholeListener extends AbstractAnnotatedListener
    {
        @OnWebSocketMessage
        public void onMessage(byte[] message)
        {
            sendBinary(BufferUtil.toBuffer(message), true);
        }
    }

    @WebSocket
    public static class AnnotatedOffsetByteArrayWholeListener extends AbstractAnnotatedListener
    {
        @OnWebSocketMessage
        public void onMessage(byte[] message, int offset, int length)
        {
            sendBinary(BufferUtil.toBuffer(message, offset, length), true);
        }
    }

    @WebSocket
    public static class AnnotatedInputStreamWholeListener extends AbstractAnnotatedListener
    {
        @OnWebSocketMessage
        public void onMessage(InputStream stream)
        {
            sendBinary(readBytes(stream), true);
        }
    }

    @WebSocket
    public static class AnnotatedReverseArgumentPartialListener extends AbstractAnnotatedListener
    {
        @OnWebSocketMessage
        public void onMessage(Session session, ByteBuffer message)
        {
            sendBinary(message, true);
        }
    }

    public static ByteBuffer readBytes(InputStream stream)
    {
        try
        {
            return BufferUtil.toBuffer(IO.readBytes(stream));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}