package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

/**
 * WebSocket Frame Listener interface for incoming WebSocket frames.
 */
public interface WebSocketFrameListener extends WebSocketConnectionListener
{
    /**
     * A WebSocket frame has been received.
     *
     * @param frame the immutable frame received
     */
    void onWebSocketFrame(Frame frame);
}