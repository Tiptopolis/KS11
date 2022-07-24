package com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

public class JettyLoggingServiceProvider implements SLF4JServiceProvider
{
    /**
     * Declare the version of the SLF4J API this implementation is compiled against.
     */
    private static final String REQUESTED_API_VERSION = "2.0";

    private JettyLoggerFactory loggerFactory;
    private BasicMarkerFactory markerFactory;
    private MDCAdapter mdcAdapter;

    @Override
    public void initialize()
    {
        JettyLoggerConfiguration config = new JettyLoggerConfiguration().load(this.getClass().getClassLoader());
        loggerFactory = new JettyLoggerFactory(config);
        markerFactory = new BasicMarkerFactory();
        mdcAdapter = new NOPMDCAdapter(); // TODO: Provide Jetty Implementation?
    }

    public JettyLoggerFactory getJettyLoggerFactory()
    {
        return loggerFactory;
    }

    @Override
    public ILoggerFactory getLoggerFactory()
    {
        return getJettyLoggerFactory();
    }

    @Override
    public IMarkerFactory getMarkerFactory()
    {
        return markerFactory;
    }

    @Override
    public MDCAdapter getMDCAdapter()
    {
        return mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion()
    {
        return REQUESTED_API_VERSION;
    }
}