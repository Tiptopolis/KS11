package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.listener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * IntrospectorCleaner
 *
 * Cleans a static cache of Methods held by java.beans.Introspector
 * class when a context is undeployed.
 *
 * @see java.beans.Introspector
 */
public class IntrospectorCleaner implements ServletContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        java.beans.Introspector.flushCaches();
    }
}