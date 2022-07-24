package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;

/**
 * Per spec, a protocol error should result in a Close frame of status code 1002 (PROTOCOL_ERROR)
 *
 * @see <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC6455 : Section 7.4.1</a>
 */
@SuppressWarnings("serial")
public class ProtocolException extends CloseException
{
    public ProtocolException(String message)
    {
        super(CloseStatus.PROTOCOL, message);
    }

    public ProtocolException(String message, Throwable t)
    {
        super(CloseStatus.PROTOCOL, message, t);
    }

    public ProtocolException(Throwable t)
    {
        super(CloseStatus.PROTOCOL, t);
    }
}