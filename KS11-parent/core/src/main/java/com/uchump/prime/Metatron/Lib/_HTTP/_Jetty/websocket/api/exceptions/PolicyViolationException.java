package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;

/**
 * Exception when a violation of policy occurs and should trigger a connection close.
 *
 * @see StatusCode#POLICY_VIOLATION
 */
@SuppressWarnings("serial")
public class PolicyViolationException extends CloseException
{
    public PolicyViolationException(String message)
    {
        super(StatusCode.POLICY_VIOLATION, message);
    }

    public PolicyViolationException(String message, Throwable t)
    {
        super(StatusCode.POLICY_VIOLATION, message, t);
    }

    public PolicyViolationException(Throwable t)
    {
        super(StatusCode.POLICY_VIOLATION, t);
    }
}