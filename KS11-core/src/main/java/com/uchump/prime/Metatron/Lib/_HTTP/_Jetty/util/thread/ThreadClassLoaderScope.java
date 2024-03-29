package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread;

import java.io.Closeable;

public class ThreadClassLoaderScope implements Closeable
{
    private final ClassLoader old;
    private final ClassLoader scopedClassLoader;

    public ThreadClassLoaderScope(ClassLoader cl)
    {
        old = Thread.currentThread().getContextClassLoader();
        scopedClassLoader = cl;
        Thread.currentThread().setContextClassLoader(scopedClassLoader);
    }

    @Override
    public void close()
    {
        Thread.currentThread().setContextClassLoader(old);
    }

    public ClassLoader getScopedClassLoader()
    {
        return scopedClassLoader;
    }
}