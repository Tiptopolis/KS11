package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import java.util.EventListener;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ListenerHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.Source;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.DiscoveredAnnotation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaData;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.Origin;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

/**
 * WebListenerAnnotation
 */
public class WebListenerAnnotation extends DiscoveredAnnotation
{
    private static final Logger LOG = LoggerFactory.getLogger(WebListenerAnnotation.class);

    public WebListenerAnnotation(WebAppContext context, String className)
    {
        super(context, className);
    }

    public WebListenerAnnotation(WebAppContext context, String className, Resource resource)
    {
        super(context, className, resource);
    }

    @Override
    public void apply()
    {
        Class<? extends java.util.EventListener> clazz = (Class<? extends EventListener>)getTargetClass();

        if (clazz == null)
        {
            LOG.warn("{} cannot be loaded", _className);
            return;
        }

        try
        {
            if (ServletContextListener.class.isAssignableFrom(clazz) ||
                ServletContextAttributeListener.class.isAssignableFrom(clazz) ||
                ServletRequestListener.class.isAssignableFrom(clazz) ||
                ServletRequestAttributeListener.class.isAssignableFrom(clazz) ||
                HttpSessionListener.class.isAssignableFrom(clazz) ||
                HttpSessionAttributeListener.class.isAssignableFrom(clazz) ||
                HttpSessionIdListener.class.isAssignableFrom(clazz))
            {
                MetaData metaData = _context.getMetaData();
                if (metaData.getOrigin(clazz.getName() + ".listener") == Origin.NotSet)
                {
                    ListenerHolder h = _context.getServletHandler().newListenerHolder(new Source(Source.Origin.ANNOTATION, clazz.getName()));
                    h.setHeldClass(clazz);
                    _context.getServletHandler().addListener(h);
                    metaData.setOrigin(clazz.getName() + ".listener", clazz.getAnnotation(WebListener.class), clazz);
                }
            }
            else
                LOG.warn("{} does not implement one of the servlet listener interfaces", clazz.getName());
        }
        catch (Exception e)
        {
            LOG.warn("Unable to add listener {}", clazz, e);
        }
    }
}