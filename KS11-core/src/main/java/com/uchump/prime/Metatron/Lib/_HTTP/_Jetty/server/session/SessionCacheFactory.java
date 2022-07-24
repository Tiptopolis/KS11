package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;
/**
 * SessionCacheFactory
 */
public interface SessionCacheFactory
{
    SessionCache getSessionCache(SessionHandler handler);
}