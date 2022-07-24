package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.InvokerUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.ReflectUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketFrameHandlerFactory;

public class CompletableFutureMethodHandle
{
    public static <T> MethodHandle of(Class<T> type, CompletableFuture<T> future)
    {
        Method method = ReflectUtils.findMethod(CompletableFuture.class, "complete", type);
        MethodHandles.Lookup lookup = JavaxWebSocketFrameHandlerFactory.getServerMethodHandleLookup();
        MethodHandle completeHandle = InvokerUtils.mutatedInvoker(lookup, CompletableFuture.class, method, new InvokerUtils.Arg(type));
        return completeHandle.bindTo(future);
    }
}