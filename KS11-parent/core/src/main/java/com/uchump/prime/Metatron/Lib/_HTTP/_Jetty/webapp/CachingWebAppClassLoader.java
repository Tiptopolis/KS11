package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;


import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedOperation;

/**
 * A WebAppClassLoader that caches {@link #getResource(String)} results.
 * Specifically this ClassLoader caches not found classes and resources,
 * which can greatly increase performance for applications that search
 * for resources.
 */
@ManagedObject
public class CachingWebAppClassLoader extends WebAppClassLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(CachingWebAppClassLoader.class);

    private final Set<String> _notFound = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<String, URL> _cache = new ConcurrentHashMap<>();

    public CachingWebAppClassLoader(ClassLoader parent, Context context) throws IOException
    {
        super(parent, context);
    }

    public CachingWebAppClassLoader(Context context) throws IOException
    {
        super(context);
    }

    @Override
    public URL getResource(String name)
    {
        if (_notFound.contains(name))
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Not found cache hit resource {}", name);
            return null;
        }

        URL url = _cache.get(name);

        if (url == null)
        {
            // Not found in cache, try parent
            url = super.getResource(name);

            if (url == null)
            {
                // Still not found, cache the not-found result
                if (LOG.isDebugEnabled())
                    LOG.debug("Caching not found resource {}", name);
                _notFound.add(name);
            }
            else
            {
                // Cache the new result
                _cache.putIfAbsent(name, url);
            }
        }

        return url;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        if (_notFound.contains(name))
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Not found cache hit resource {}", name);
            throw new ClassNotFoundException(name + ": in notfound cache");
        }
        try
        {
            return super.loadClass(name);
        }
        catch (ClassNotFoundException nfe)
        {
            if (_notFound.add(name))
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Caching not found {}", name, nfe);
                }
            throw nfe;
        }
    }

    @ManagedOperation
    public void clearCache()
    {
        _cache.clear();
        _notFound.clear();
    }

    @Override
    public String toString()
    {
        return "Caching[" + super.toString() + "]";
    }
}