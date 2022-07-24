package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Utility to obtain the {@link Function#apply(Object)} method as a {@link MethodHandle}
 */
public class FunctionMethod
{
    private static final Method functionApplyMethod;
    private static final MethodHandle functionApplyMethodHandle;

    static
    {
        Method foundMethod = null;

        for (Method method : Function.class.getDeclaredMethods())
        {
            if (method.getName().equals("apply") && method.getParameterCount() == 1)
            {
                foundMethod = method;
                break;
            }
        }

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        functionApplyMethod = foundMethod;
        try
        {
            functionApplyMethodHandle = lookup.unreflect(functionApplyMethod);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Unable to access: " + functionApplyMethod, e);
        }
    }

    public static MethodHandle getFunctionApplyMethodHandle()
    {
        return functionApplyMethodHandle;
    }
}