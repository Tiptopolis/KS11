package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

/**
 * Interface for Listeners that are interested in knowing about the Session history.
 */
public interface WebSocketSessionListener
{
    default void onWebSocketSessionCreated(Session session)
    {
    }

    default void onWebSocketSessionOpened(Session session)
    {
    }

    default void onWebSocketSessionClosed(Session session)
    {
    }
}