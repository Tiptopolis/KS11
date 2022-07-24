package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

import java.nio.ByteBuffer;

/**
 * WebSocket Partial Message Listener interface for incoming WebSocket TEXT/BINARY/CONTINUATION frames.
 */
public interface WebSocketPartialListener extends WebSocketConnectionListener
{
    /**
     * A WebSocket BINARY (or associated CONTINUATION) frame has been received.
     * <p>
     * <b>Important Note</b>: The payload {@code ByteBuffer} cannot be modified, and the ByteBuffer object itself
     * will be recycled on completion of this method call, make a copy of the data contained within if you want to
     * retain it between calls.
     *
     * @param payload the binary message frame payload
     * @param fin true if this is the final frame, false otherwise
     */
    default void onWebSocketPartialBinary(ByteBuffer payload, boolean fin)
    {
    }

    /**
     * A WebSocket TEXT (or associated CONTINUATION) frame has been received.
     *
     * @param payload the text message payload
     * <p>
     * Note that due to framing, there is a above average chance of any UTF8 sequences being split on the
     * border between two frames will result in either the previous frame, or the next frame having an
     * invalid UTF8 sequence, but the combined frames having a valid UTF8 sequence.
     * <p>
     * The String being provided here will not end in a split UTF8 sequence. Instead this partial sequence
     * will be held over until the next frame is received.
     * @param fin true if this is the final frame, false otherwise
     */
    default void onWebSocketPartialText(String payload, boolean fin)
    {
    }
}