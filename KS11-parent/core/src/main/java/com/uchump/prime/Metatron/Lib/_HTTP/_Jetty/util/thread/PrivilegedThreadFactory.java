package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.function.Supplier;

/**
 * Convenience class to ensure that a new Thread is created
 * inside a privileged block. 
 * 
 * This prevents the Thread constructor
 * from pinning the caller's context classloader. This happens
 * when the Thread constructor takes a snapshot of the current
 * calling context - which contains ProtectionDomains that may
 * reference the context classloader - and remembers it for the
 * lifetime of the Thread.
 */
class PrivilegedThreadFactory
{
    /**
     * Use a Supplier to make a new thread, calling it within
     * a privileged block to prevent classloader pinning.
     * 
     * @param newThreadSupplier a Supplier to create a fresh thread
     * @return a new thread, protected from classloader pinning.
     */
    static <T extends Thread> T newThread(Supplier<T> newThreadSupplier)
    {
        return AccessController.doPrivileged(new PrivilegedAction<T>()
        {
            @Override
            public T run()
            {
                return newThreadSupplier.get();
            }
        });
    }
}