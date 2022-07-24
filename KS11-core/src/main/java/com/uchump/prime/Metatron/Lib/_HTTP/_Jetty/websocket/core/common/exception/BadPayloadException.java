package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;

/**
 * Exception to terminate the connection because it has received data within a frame payload that was not consistent with the requirements of that frame
 * payload. (eg: not UTF-8 in a text frame, or a unexpected data seen by an extension)
 *
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC6455 : Section 7.4.1</a>
 */
@SuppressWarnings("serial")
public class BadPayloadException extends CloseException
{
    public BadPayloadException(String message)
    {
        super(CloseStatus.BAD_PAYLOAD, message);
    }

    public BadPayloadException(String message, Throwable t)
    {
        super(CloseStatus.BAD_PAYLOAD, message, t);
    }

    public BadPayloadException(Throwable t)
    {
        super(CloseStatus.BAD_PAYLOAD, t);
    }
}