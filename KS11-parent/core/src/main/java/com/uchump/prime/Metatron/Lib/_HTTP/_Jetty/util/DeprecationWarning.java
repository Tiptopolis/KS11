package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeprecationWarning implements Decorator
{
    private static final Logger LOG = LoggerFactory.getLogger(DeprecationWarning.class);

    @Override
    public <T> T decorate(T o)
    {
        if (o == null)
        {
            return null;
        }

        Class<?> clazz = o.getClass();

        try
        {
            Deprecated depr = clazz.getAnnotation(Deprecated.class);
            if (depr != null)
            {
                LOG.warn("Using @Deprecated Class {}", clazz.getName());
            }
        }
        catch (Throwable t)
        {
            LOG.trace("IGNORED", t);
        }

        verifyIndirectTypes(clazz.getSuperclass(), clazz, "Class");
        for (Class<?> ifaceClazz : clazz.getInterfaces())
        {
            verifyIndirectTypes(ifaceClazz, clazz, "Interface");
        }

        return o;
    }

    private void verifyIndirectTypes(Class<?> superClazz, Class<?> clazz, String typeName)
    {
        try
        {
            // Report on super class deprecation too
            while (superClazz != null && superClazz != Object.class)
            {
                Deprecated supDepr = superClazz.getAnnotation(Deprecated.class);
                if (supDepr != null)
                {
                    LOG.warn("Using indirect @Deprecated {} {} - (seen from {})", typeName, superClazz.getName(), clazz);
                }

                superClazz = superClazz.getSuperclass();
            }
        }
        catch (Throwable t)
        {
            LOG.trace("IGNORED", t);
        }
    }

    @Override
    public void destroy(Object o)
    {
    }
}