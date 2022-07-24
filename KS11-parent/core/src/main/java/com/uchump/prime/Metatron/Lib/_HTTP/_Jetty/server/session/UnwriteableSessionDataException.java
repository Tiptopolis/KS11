package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;

/**
 * UnwriteableSessionDataException
 */
public class UnwriteableSessionDataException extends Exception
{
    private String _id;
    private SessionContext _sessionContext;

    public UnwriteableSessionDataException(String id, SessionContext contextId, Throwable t)
    {
        super("Unwriteable session " + id + " for " + contextId, t);
        _id = id;
    }

    public String getId()
    {
        return _id;
    }

    public SessionContext getSessionContext()
    {
        return _sessionContext;
    }
}