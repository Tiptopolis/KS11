package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;

/**
 * NullSessionDataStoreFactory
 */
public class NullSessionDataStoreFactory extends AbstractSessionDataStoreFactory
{

    @Override
    public SessionDataStore getSessionDataStore(SessionHandler handler) throws Exception
    {
        return new NullSessionDataStore();
    }
}