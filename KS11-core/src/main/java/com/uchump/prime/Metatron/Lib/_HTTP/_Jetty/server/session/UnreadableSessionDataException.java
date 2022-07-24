package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;
/**
 * UnreadableSessionDataException
 */
public class UnreadableSessionDataException extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 1806303483488966566L;
    private String _id;
    private SessionContext _sessionContext;

    /**
     * @return the session id
     */
    public String getId()
    {
        return _id;
    }

    /**
     * @return the SessionContext to which the unreadable session belongs
     */
    public SessionContext getSessionContext()
    {
        return _sessionContext;
    }

    /**
     * @param id the session id
     * @param sessionContext the sessionContext
     * @param t the cause of the exception
     */
    public UnreadableSessionDataException(String id, SessionContext sessionContext, Throwable t)
    {
        super("Unreadable session " + id + " for " + sessionContext, t);
        _sessionContext = sessionContext;
        _id = id;
    }
}