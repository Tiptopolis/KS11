package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io;
/**
 * Subclass of {@link java.lang.RuntimeException} used to signal that there
 * was an {@link java.io.IOException} thrown by underlying {@link java.io.Writer}
 */
public class RuntimeIOException extends RuntimeException
{
    public RuntimeIOException()
    {
        super();
    }

    public RuntimeIOException(String message)
    {
        super(message);
    }

    public RuntimeIOException(Throwable cause)
    {
        super(cause);
    }

    public RuntimeIOException(String message, Throwable cause)
    {
        super(message, cause);
    }
}