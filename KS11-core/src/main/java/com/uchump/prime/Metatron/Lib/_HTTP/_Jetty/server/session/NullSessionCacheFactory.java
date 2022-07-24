package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NullSessionCacheFactory
 *
 * Factory for NullSessionCaches.
 */
public class NullSessionCacheFactory extends AbstractSessionCacheFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(NullSessionCacheFactory.class);
    
    @Override
    public int getEvictionPolicy()
    {
        return SessionCache.EVICT_ON_SESSION_EXIT; //never actually stored
    }

    @Override
    public void setEvictionPolicy(int evictionPolicy)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Ignoring eviction policy setting for NullSessionCaches");
    }

    @Override
    public boolean isSaveOnInactiveEvict()
    {
        return false; //never kept in cache
    }

    @Override
    public void setSaveOnInactiveEvict(boolean saveOnInactiveEvict)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Ignoring eviction policy setting for NullSessionCaches");
    }
    
    @Override
    public boolean isInvalidateOnShutdown()
    {
        return false; //meaningless for NullSessionCache
    }

    @Override
    public void setInvalidateOnShutdown(boolean invalidateOnShutdown)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Ignoring invalidateOnShutdown setting for NullSessionCaches");
    }

    @Override
    public SessionCache newSessionCache(SessionHandler handler)
    {
        return new NullSessionCache(handler);
    }
}