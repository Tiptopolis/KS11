package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

/**
 * Callback for Write events.
 * <p>
 * NOTE: We don't expose org.eclipse.jetty.util.Callback here as that would complicate matters with the WebAppContext's classloader isolation.
 */
public interface WriteCallback
{
    WriteCallback NOOP = new WriteCallback()
    {
    };

    /**
     * <p>
     * Callback invoked when the write fails.
     * </p>
     *
     * @param x the reason for the write failure
     */
    default void writeFailed(Throwable x)
    {
    }

    /**
     * <p>
     * Callback invoked when the write succeeds.
     * </p>
     *
     * @see #writeFailed(Throwable)
     */
    default void writeSuccess()
    {
    }

    @Deprecated
    class Adaptor implements WriteCallback
    {
        @Override
        public void writeFailed(Throwable x)
        {
        }

        @Override
        public void writeSuccess()
        {
        }
    }
}