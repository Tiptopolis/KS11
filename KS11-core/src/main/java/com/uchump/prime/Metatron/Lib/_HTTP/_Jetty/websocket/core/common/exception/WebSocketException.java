package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception;
/**
 * A recoverable exception within the websocket framework.
 */
@SuppressWarnings("serial")
public class WebSocketException extends RuntimeException
{
    public WebSocketException()
    {
        super();
    }

    public WebSocketException(String message)
    {
        super(message);
    }

    public WebSocketException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public WebSocketException(Throwable cause)
    {
        super(cause);
    }
}