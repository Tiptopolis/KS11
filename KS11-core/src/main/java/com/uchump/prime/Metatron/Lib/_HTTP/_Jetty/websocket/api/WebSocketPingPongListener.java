package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

import java.nio.ByteBuffer;

/**
 * WebSocket PING/PONG Listener interface for incoming WebSocket PING/PONG frames.
 */
public interface WebSocketPingPongListener extends WebSocketConnectionListener
{
    /**
     * A WebSocket PING has been received.
     *
     * @param payload the ping payload
     */
    default void onWebSocketPing(ByteBuffer payload)
    {
    }

    /**
     * A WebSocket PONG has been received.
     *
     * @param payload the pong payload
     */
    default void onWebSocketPong(ByteBuffer payload)
    {
    }
}