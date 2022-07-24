package com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A try-with-resources compatible layer for {@link JettyLogger#setHideStacks(boolean) hiding stacktraces} within the scope of the <code>try</code> block when
 * logging with {@link JettyLogger} implementation.
 * <p>
 * Use of other logging implementation cause no effect when using this class
 * <p>
 * Example:
 *
 * <pre>
 * try (StacklessLogging scope = new StacklessLogging(EventDriver.class,Noisy.class))
 * {
 *     doActionThatCausesStackTraces();
 * }
 * </pre>
 */
public class StacklessLogging implements AutoCloseable
{
    private static final Logger LOG = LoggerFactory.getLogger(StacklessLogging.class);
    private static final JettyLoggerFactory loggerFactory;

    static
    {
        JettyLoggerFactory jettyLoggerFactory = null;
        ILoggerFactory activeLoggerFactory = LoggerFactory.getILoggerFactory();
        if (activeLoggerFactory instanceof JettyLoggerFactory)
        {
            jettyLoggerFactory = (JettyLoggerFactory)activeLoggerFactory;
        }
        else
        {
            LOG.warn("Unable to squelch stacktraces ({} is not a {})",
                activeLoggerFactory.getClass().getName(),
                JettyLoggerFactory.class.getName());
        }
        loggerFactory = jettyLoggerFactory;
    }

    private final Set<JettyLogger> squelched = new HashSet<>();

    public StacklessLogging(Class<?>... classesToSquelch)
    {
        for (Class<?> clazz : classesToSquelch)
        {
            JettyLogger jettyLogger = loggerFactory.getJettyLogger(clazz.getName());
            if (!jettyLogger.isDebugEnabled())
            {
                if (!jettyLogger.isHideStacks())
                {
                    jettyLogger.setHideStacks(true);
                    squelched.add(jettyLogger);
                }
            }
        }
    }

    public StacklessLogging(Package... packagesToSquelch)
    {
        for (Package pkg : packagesToSquelch)
        {
            JettyLogger jettyLogger = loggerFactory.getJettyLogger(pkg.getName());
            if (!jettyLogger.isDebugEnabled())
            {
                if (!jettyLogger.isHideStacks())
                {
                    jettyLogger.setHideStacks(true);
                    squelched.add(jettyLogger);
                }
            }
        }
    }

    public StacklessLogging(Logger... logs)
    {
        for (Logger log : logs)
        {
            if (log instanceof JettyLogger && !log.isDebugEnabled())
            {
                JettyLogger jettyLogger = ((JettyLogger)log);
                if (!jettyLogger.isHideStacks())
                {
                    jettyLogger.setHideStacks(true);
                    squelched.add(jettyLogger);
                }
            }
        }
    }

    @Override
    public void close()
    {
        for (JettyLogger log : squelched)
        {
            log.setHideStacks(false);
        }
    }
}