package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io;

import java.io.EOFException;

/**
 * A Jetty specialization of EOFException.
 * <p> This is thrown by Jetty to distinguish between EOF received from
 * the connection, vs and EOF thrown by some application talking to some other file/socket etc.
 * The only difference in handling is that Jetty EOFs are logged less verbosely.
 */
public class EofException extends EOFException implements QuietException
{
    public EofException()
    {
    }

    public EofException(String reason)
    {
        super(reason);
    }

    public EofException(Throwable th)
    {
        if (th != null)
            initCause(th);
    }
}