package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;

public final class StackUtils
{
    public static String toString(Throwable t)
    {
        try (StringWriter w = new StringWriter())
        {
            try (PrintWriter out = new PrintWriter(w))
            {
                t.printStackTrace(out);
                return w.toString();
            }
        }
        catch (IOException e)
        {
            return "Unable to get stacktrace for: " + t;
        }
    }

    public static Supplier<String> supply(Throwable t)
    {
        return () -> toString(t);
    }
}