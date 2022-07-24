package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.preventers;
import java.sql.DriverManager;

/**
 * DriverManagerLeakPreventer
 *
 * Cause DriverManager.getCallerClassLoader() to be called, which will pin the classloader.
 *
 * Inspired by Tomcat JreMemoryLeakPrevention.
 */
public class DriverManagerLeakPreventer extends AbstractLeakPreventer
{

    @Override
    public void prevent(ClassLoader loader)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Pinning DriverManager classloader with {}", loader);
        DriverManager.getDrivers();
    }
}