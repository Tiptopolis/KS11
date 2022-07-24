package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.security.GeneralSecurityException;

/**
 * @version $Rev: 4466 $ $Date: 2009-02-10 23:42:54 +0100 (Tue, 10 Feb 2009) $
 */
public class ServerAuthException extends GeneralSecurityException
{

    public ServerAuthException()
    {
    }

    public ServerAuthException(String s)
    {
        super(s);
    }

    public ServerAuthException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ServerAuthException(Throwable throwable)
    {
        super(throwable);
    }
}