package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;

/**
 * This exception can safely be stored in a static variable as suppressed exceptions are disabled,
 * meaning calling {@link #addSuppressed(Throwable)} has no effect.
 * This prevents potential memory leaks where a statically-stored exception would accumulate
 * suppressed exceptions added to them.
 */
public class StaticException extends Exception
{
    /**
     * Create an instance with writable stack trace and suppression disabled.
     *
     * @param message – the detail message
     *
     * @see Throwable#Throwable(String, Throwable, boolean, boolean)
     */
    public StaticException(String message)
    {
        this(message, false);
    }

    /**
     * Create an instance with suppression disabled.
     *
     * @param message – the detail message
     * @param writableStackTrace whether or not the stack trace should be writable
     *
     * @see Throwable#Throwable(String, Throwable, boolean, boolean)
     */
    public StaticException(String message, boolean writableStackTrace)
    {
        // Make sure to call the super constructor that disables suppressed exception.
        super(message, null, false, writableStackTrace);
    }
}