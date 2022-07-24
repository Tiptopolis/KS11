package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.internal;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.websocket.server.PathParam;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.InvalidSignatureException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.InvokerUtils;

/**
 * Method argument identifier for {@link javax.websocket.server.PathParam} annotations.
 */
@SuppressWarnings("unused")
public class PathParamIdentifier implements InvokerUtils.ParamIdentifier
{
    @Override
    public InvokerUtils.Arg getParamArg(Method method, Class<?> paramType, int idx)
    {
        Annotation[] annos = method.getParameterAnnotations()[idx];
        if (annos != null || (annos.length > 0))
        {
            for (Annotation anno : annos)
            {
                if (anno.annotationType().equals(PathParam.class))
                {
                    validateType(paramType);
                    PathParam pathParam = (PathParam)anno;
                    return new InvokerUtils.Arg(paramType, pathParam.value());
                }
            }
        }
        return new InvokerUtils.Arg(paramType);
    }

    /**
     * The JSR356 rules for @PathParam only support
     * String, Primitive Types (and their Boxed version)
     */
    public static void validateType(Class<?> type)
    {
        if (!String.class.isAssignableFrom(type) &&
            !Integer.class.isAssignableFrom(type) &&
            !Integer.TYPE.isAssignableFrom(type) &&
            !Long.class.isAssignableFrom(type) &&
            !Long.TYPE.isAssignableFrom(type) &&
            !Short.class.isAssignableFrom(type) &&
            !Short.TYPE.isAssignableFrom(type) &&
            !Float.class.isAssignableFrom(type) &&
            !Float.TYPE.isAssignableFrom(type) &&
            !Double.class.isAssignableFrom(type) &&
            !Double.TYPE.isAssignableFrom(type) &&
            !Boolean.class.isAssignableFrom(type) &&
            !Boolean.TYPE.isAssignableFrom(type) &&
            !Character.class.isAssignableFrom(type) &&
            !Character.TYPE.isAssignableFrom(type) &&
            !Byte.class.isAssignableFrom(type) &&
            !Byte.TYPE.isAssignableFrom(type))
            throw new InvalidSignatureException("Unsupported PathParam Type: " + type);
    }
}