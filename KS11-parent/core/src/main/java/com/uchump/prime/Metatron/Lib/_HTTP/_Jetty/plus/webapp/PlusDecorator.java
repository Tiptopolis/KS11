package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.webapp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.annotation.InjectionCollection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.annotation.LifeCycleCallbackCollection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Decorator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

/**
 * PlusDecorator
 */
public class PlusDecorator implements Decorator
{
    private static final Logger LOG = LoggerFactory.getLogger(PlusDecorator.class);

    protected WebAppContext _context;

    public PlusDecorator(WebAppContext context)
    {
        _context = context;
    }

    @Override
    public Object decorate(Object o)
    {
        InjectionCollection injections = (InjectionCollection)_context.getAttribute(InjectionCollection.INJECTION_COLLECTION);
        if (injections != null)
            injections.inject(o);

        LifeCycleCallbackCollection callbacks = (LifeCycleCallbackCollection)_context.getAttribute(LifeCycleCallbackCollection.LIFECYCLE_CALLBACK_COLLECTION);
        if (callbacks != null)
        {
            try
            {
                callbacks.callPostConstructCallback(o);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return o;
    }

    @Override
    public void destroy(Object o)
    {
        LifeCycleCallbackCollection callbacks = (LifeCycleCallbackCollection)_context.getAttribute(LifeCycleCallbackCollection.LIFECYCLE_CALLBACK_COLLECTION);
        if (callbacks != null)
        {
            try
            {
                callbacks.callPreDestroyCallback(o);
            }
            catch (Exception e)
            {
                LOG.warn("Destroying instance of {}", o.getClass(), e);
            }
        }
    }
}