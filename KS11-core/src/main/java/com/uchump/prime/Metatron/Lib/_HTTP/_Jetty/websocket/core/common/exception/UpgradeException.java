package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception;

import java.net.URI;

/**
 * Exception during WebSocket Upgrade Handshake.
 */
@SuppressWarnings("serial")
public class UpgradeException extends WebSocketException
{
    private final URI requestURI;
    private final int responseStatusCode;

    public UpgradeException(URI requestURI, int responseStatusCode, String message)
    {
        super(message);
        this.requestURI = requestURI;
        this.responseStatusCode = responseStatusCode;
    }

    public UpgradeException(URI requestURI, int responseStatusCode, String message, Throwable cause)
    {
        super(message, cause);
        this.requestURI = requestURI;
        this.responseStatusCode = responseStatusCode;
    }

    public UpgradeException(URI requestURI, Throwable cause)
    {
        super(cause);
        this.requestURI = requestURI;
        this.responseStatusCode = -1;
    }

    public URI getRequestURI()
    {
        return requestURI;
    }

    public int getResponseStatusCode()
    {
        return responseStatusCode;
    }
}