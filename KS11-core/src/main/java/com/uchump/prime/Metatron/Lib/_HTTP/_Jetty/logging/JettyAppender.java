package com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging;

import org.slf4j.event.Level;

public interface JettyAppender
{
    void emit(JettyLogger logger, Level level, long timestamp, String threadName,
              Throwable throwable, String message, Object... argumentArray);
}