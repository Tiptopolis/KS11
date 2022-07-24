package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;

/**
 * A {@link Throwable} that may be used in static contexts. It uses Java 7
 * constructor that prevents setting stackTrace inside exception object.
 */
public class ConstantThrowable extends Throwable
{
    public ConstantThrowable()
    {
        this(null);
    }

    public ConstantThrowable(String name)
    {
        super(name, null, false, false);
    }

    @Override
    public String toString()
    {
        return String.valueOf(getMessage());
    }
}