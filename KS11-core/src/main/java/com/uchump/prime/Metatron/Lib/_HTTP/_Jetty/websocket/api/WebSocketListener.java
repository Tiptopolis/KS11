package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

/**
 * Basic WebSocket Listener interface for incoming WebSocket message events.
 */
public interface WebSocketListener extends WebSocketConnectionListener
{
    /**
     * A WebSocket binary frame has been received.
     *
     * @param payload the raw payload array received
     * @param offset the offset in the payload array where the data starts
     * @param len the length of bytes in the payload
     */
    default void onWebSocketBinary(byte[] payload, int offset, int len)
    {
    }

    /**
     * A WebSocket Text frame was received.
     *
     * @param message the message
     */
    default void onWebSocketText(String message)
    {
    }
}