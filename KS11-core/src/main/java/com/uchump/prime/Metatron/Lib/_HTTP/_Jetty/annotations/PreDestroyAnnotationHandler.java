package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.annotation.PreDestroy;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationIntrospector.AbstractIntrospectableAnnotationHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.annotation.LifeCycleCallbackCollection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.annotation.PreDestroyCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaData;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.Origin;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;



public class PreDestroyAnnotationHandler extends AbstractIntrospectableAnnotationHandler
{
    public PreDestroyAnnotationHandler(WebAppContext wac)
    {
        super(true, wac);
    }

    @Override
    public void doHandle(Class clazz)
    {
        //Check that the PreDestroy is on a class that we're interested in
        if (supportsPreDestroy(clazz))
        {
            Method[] methods = clazz.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++)
            {
                Method m = (Method)methods[i];
                if (m.isAnnotationPresent(PreDestroy.class))
                {
                    if (m.getParameterCount() != 0)
                        throw new IllegalStateException(m + " has parameters");
                    if (m.getReturnType() != Void.TYPE)
                        throw new IllegalStateException(m + " is not void");
                    if (m.getExceptionTypes().length != 0)
                        throw new IllegalStateException(m + " throws checked exceptions");
                    if (Modifier.isStatic(m.getModifiers()))
                        throw new IllegalStateException(m + " is static");

                    //ServletSpec 3.0 p80 If web.xml declares even one predestroy then all predestroys
                    //in fragments must be ignored. Otherwise, they are additive.
                    MetaData metaData = _context.getMetaData();
                    Origin origin = metaData.getOrigin("pre-destroy");
                    if (origin != null &&
                        (origin == Origin.WebXml ||
                            origin == Origin.WebDefaults ||
                            origin == Origin.WebOverride))
                        return;

                    PreDestroyCallback callback = new PreDestroyCallback(clazz, m.getName());

                    LifeCycleCallbackCollection lifecycles = (LifeCycleCallbackCollection)_context.getAttribute(LifeCycleCallbackCollection.LIFECYCLE_CALLBACK_COLLECTION);
                    if (lifecycles == null)
                    {
                        lifecycles = new LifeCycleCallbackCollection();
                        _context.setAttribute(LifeCycleCallbackCollection.LIFECYCLE_CALLBACK_COLLECTION, lifecycles);
                    }

                    lifecycles.add(callback);
                }
            }
        }
    }

    /**
     * Check if the spec permits the given class to use the PreDestroy annotation.
     *
     * @param c the class
     * @return true if permitted, false otherwise
     */
    public boolean supportsPreDestroy(Class c)
    {
        if (javax.servlet.Servlet.class.isAssignableFrom(c) ||
            javax.servlet.Filter.class.isAssignableFrom(c) ||
            javax.servlet.ServletContextListener.class.isAssignableFrom(c) ||
            javax.servlet.ServletContextAttributeListener.class.isAssignableFrom(c) ||
            javax.servlet.ServletRequestListener.class.isAssignableFrom(c) ||
            javax.servlet.ServletRequestAttributeListener.class.isAssignableFrom(c) ||
            javax.servlet.http.HttpSessionListener.class.isAssignableFrom(c) ||
            javax.servlet.http.HttpSessionAttributeListener.class.isAssignableFrom(c) ||
            javax.servlet.http.HttpSessionIdListener.class.isAssignableFrom(c) ||
            javax.servlet.AsyncListener.class.isAssignableFrom(c) ||
            javax.servlet.http.HttpUpgradeHandler.class.isAssignableFrom(c))
            return true;

        return false;
    }
}