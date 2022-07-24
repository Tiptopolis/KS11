package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions;

/**
 * Exception thrown to indicate a connection I/O timeout.
 */
public class WebSocketTimeoutException extends WebSocketException
{
    private static final long serialVersionUID = -6145098200250676673L;

    public WebSocketTimeoutException(String message)
    {
        super(message);
    }

    public WebSocketTimeoutException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public WebSocketTimeoutException(Throwable cause)
    {
        super(cause);
    }
}