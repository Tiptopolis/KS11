package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PreDestroyCallback
 */
public class PreDestroyCallback extends LifeCycleCallback
{
    private static final Logger LOG = LoggerFactory.getLogger(PreDestroyCallback.class);

    /**
     * @param clazz the class object to be injected
     * @param methodName the name of the method to inject
     */
    public PreDestroyCallback(Class<?> clazz, String methodName)
    {
        super(clazz, methodName);
    }

    /**
     * @param className the name of the class to inject
     * @param methodName the name of the method to inject
     */
    public PreDestroyCallback(String className, String methodName)
    {
        super(className, methodName);
    }

    /** 
     * Commons Annotations Specification section 2.6:
     * - no params to method
     * - returns void
     * - no checked exceptions
     * - not static
     *
     * @see org.eclipse.jetty.plus.annotation.LifeCycleCallback#validate(java.lang.Class, java.lang.reflect.Method)
     */
    @Override
    public void validate(Class<?> clazz, Method method)
    {

        if (method.getExceptionTypes().length > 0)
            throw new IllegalArgumentException(clazz.getName() + "." + method.getName() + " cannot not throw a checked exception");

        if (!method.getReturnType().equals(Void.TYPE))
            throw new IllegalArgumentException(clazz.getName() + "." + method.getName() + " cannot not have a return type");

        if (Modifier.isStatic(method.getModifiers()))
            throw new IllegalArgumentException(clazz.getName() + "." + method.getName() + " cannot be static");
    }

    @Override
    public void callback(Object instance)
    {
        try
        {
            super.callback(instance);
        }
        catch (Exception e)
        {
            LOG.warn("Ignoring exception thrown on preDestroy call to {}.{}", getTargetClass(), getTarget().getName(), e);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (super.equals(o) && (o instanceof PreDestroyCallback))
            return true;
        return false;
    }
}