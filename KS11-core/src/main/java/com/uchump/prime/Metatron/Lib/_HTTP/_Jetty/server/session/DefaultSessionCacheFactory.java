package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;

/**
 * DefaultSessionCacheFactory
 *
 * Factory for creating new DefaultSessionCaches.
 */
public class DefaultSessionCacheFactory extends AbstractSessionCacheFactory
{
    @Override
    public SessionCache newSessionCache(SessionHandler handler)
    {
        return new DefaultSessionCache(handler);
    }
}