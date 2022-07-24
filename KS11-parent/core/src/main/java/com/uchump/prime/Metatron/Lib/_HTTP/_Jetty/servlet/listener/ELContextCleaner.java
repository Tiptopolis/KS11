package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.listener;


import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;
/**
 * ELContextCleaner
 *
 * Clean up BeanELResolver when the context is going out
 * of service:
 *
 * See http://java.net/jira/browse/GLASSFISH-1649
 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=353095
 */
public class ELContextCleaner implements ServletContextListener
{
    private static final Logger LOG = LoggerFactory.getLogger(ELContextCleaner.class);

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        try
        {
            //Check that the BeanELResolver class is on the classpath
            Class<?> beanELResolver = Loader.loadClass("javax.el.BeanELResolver");

            //Get a reference via reflection to the properties field which is holding class references
            Field field = getField(beanELResolver);

            field.setAccessible(true);

            //Get rid of references
            purgeEntries(field);

            if (LOG.isDebugEnabled())
                LOG.debug("javax.el.BeanELResolver purged");
        }

        catch (ClassNotFoundException e)
        {
            //BeanELResolver not on classpath, ignore
        }
        catch (SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            LOG.warn("Cannot purge classes from javax.el.BeanELResolver", e);
        }
        catch (NoSuchFieldException e)
        {
            LOG.debug("Not cleaning cached beans: no such field javax.el.BeanELResolver.properties");
        }
    }

    protected Field getField(Class<?> beanELResolver)
        throws SecurityException, NoSuchFieldException
    {
        if (beanELResolver == null)
            return null;

        return beanELResolver.getDeclaredField("properties");
    }

    protected void purgeEntries(Field properties)
        throws IllegalArgumentException, IllegalAccessException
    {
        if (properties == null)
            return;

        Map map = (Map)properties.get(null);
        if (map == null)
            return;

        Iterator<Class<?>> itor = map.keySet().iterator();
        while (itor.hasNext())
        {
            Class<?> clazz = itor.next();
            if (LOG.isDebugEnabled())
                LOG.debug("Clazz: {} loaded by {}", clazz, clazz.getClassLoader());
            if (Thread.currentThread().getContextClassLoader().equals(clazz.getClassLoader()))
            {
                itor.remove();
                if (LOG.isDebugEnabled())
                    LOG.debug("removed");
            }
            else
            {
                if (LOG.isDebugEnabled())
                    LOG.debug("not removed: contextclassloader={} clazz's classloader={}", Thread.currentThread().getContextClassLoader(), clazz.getClassLoader());
            }
        }
    }
}