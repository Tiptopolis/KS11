package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.listeners;

import java.io.IOException;
import java.io.Reader;
import java.util.stream.Stream;


import org.junit.jupiter.params.provider.Arguments;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IO;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPartialListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;

public class TextListeners
{
    public static Stream<Arguments> getTextListeners()
    {
        return Stream.of(
            StringWholeListener.class,
            StringPartialListener.class,
            AnnotatedStringWholeListener.class,
            AnnotatedReaderWholeListener.class,
            AnnotatedReverseArgumentPartialListener.class
        ).map(Arguments::of);
    }

    public static class StringWholeListener extends AbstractListener implements WebSocketListener
    {
        @Override
        public void onWebSocketText(String message)
        {
            sendText(message, true);
        }
    }

    public static class StringPartialListener extends AbstractListener implements WebSocketPartialListener
    {
        @Override
        public void onWebSocketPartialText(String message, boolean fin)
        {
            sendText(message, fin);
        }
    }

    @WebSocket
    public static class AnnotatedStringWholeListener extends AbstractAnnotatedListener
    {
        @OnWebSocketMessage
        public void onMessage(String message)
        {
            sendText(message, true);
        }
    }

    @WebSocket
    public static class AnnotatedReaderWholeListener extends AbstractAnnotatedListener
    {
        @OnWebSocketMessage
        public void onMessage(Reader reader)
        {
            sendText(readString(reader), true);
        }
    }

    @WebSocket
    public static class AnnotatedReverseArgumentPartialListener extends AbstractAnnotatedListener
    {
        @OnWebSocketMessage
        public void onMessage(Session session, String message)
        {
            sendText(message, true);
        }
    }

    public static String readString(Reader reader)
    {
        try
        {
            return IO.toString(reader);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}