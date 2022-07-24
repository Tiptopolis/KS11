package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception;
/**
 * Indicating that the provided Class is not a valid WebSocket per the chosen API.
 */
@SuppressWarnings("serial")
public class InvalidWebSocketException extends WebSocketException
{
    public InvalidWebSocketException(String message)
    {
        super(message);
    }

    public InvalidWebSocketException(String message, Throwable cause)
    {
        super(message, cause);
    }
}