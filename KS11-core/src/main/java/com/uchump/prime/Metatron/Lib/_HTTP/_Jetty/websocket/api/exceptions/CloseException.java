package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions;

@SuppressWarnings("serial")
public class CloseException extends WebSocketException
{
    private final int statusCode;

    public CloseException(int closeCode, String message)
    {
        super(message);
        this.statusCode = closeCode;
    }

    public CloseException(int closeCode, String message, Throwable cause)
    {
        super(message, cause);
        this.statusCode = closeCode;
    }

    public CloseException(int closeCode, Throwable cause)
    {
        super(cause);
        this.statusCode = closeCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}