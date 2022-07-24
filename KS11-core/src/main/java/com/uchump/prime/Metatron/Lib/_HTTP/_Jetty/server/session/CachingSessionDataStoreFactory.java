package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;
/**
 * CachingSessionDataStoreFactory
 */
public class CachingSessionDataStoreFactory extends AbstractSessionDataStoreFactory
{

    /**
     * The SessionDataStore that will store session data.
     */
    protected SessionDataStoreFactory _sessionStoreFactory;

    protected SessionDataMapFactory _mapFactory;

    /**
     * @return the SessionDataMapFactory
     */
    public SessionDataMapFactory getMapFactory()
    {
        return _mapFactory;
    }

    /**
     * @param mapFactory the SessionDataMapFactory
     */
    public void setSessionDataMapFactory(SessionDataMapFactory mapFactory)
    {
        _mapFactory = mapFactory;
    }

    /**
     * @param factory The factory for the actual SessionDataStore that the
     * CachingSessionDataStore will delegate to
     */
    public void setSessionStoreFactory(SessionDataStoreFactory factory)
    {
        _sessionStoreFactory = factory;
    }

    @Override
    public SessionDataStore getSessionDataStore(SessionHandler handler) throws Exception
    {
        return new CachingSessionDataStore(_mapFactory.getSessionDataMap(), _sessionStoreFactory.getSessionDataStore(handler));
    }
}