package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;
/**
 * SessionDataStoreFactory
 */
public interface SessionDataStoreFactory
{
    SessionDataStore getSessionDataStore(SessionHandler handler) throws Exception;
}