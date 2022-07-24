package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;
import java.io.IOException;

import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.AbstractLifeCycle;

/**
 * Request log writer using a Slf4jLog Logger
 */
@ManagedObject("Slf4j RequestLog Writer")
public class Slf4jRequestLogWriter extends AbstractLifeCycle implements RequestLog.Writer
{
    private org.slf4j.Logger logger;
    private String loggerName;

    public Slf4jRequestLogWriter()
    {
        // Default logger name (can be set)
        this.loggerName = "org.eclipse.jetty.server.RequestLog";
    }

    public void setLoggerName(String loggerName)
    {
        this.loggerName = loggerName;
    }

    @ManagedAttribute("logger name")
    public String getLoggerName()
    {
        return loggerName;
    }

    protected boolean isEnabled()
    {
        return logger != null;
    }

    @Override
    public void write(String requestEntry) throws IOException
    {
        logger.info(requestEntry);
    }

    @Override
    protected void doStart() throws Exception
    {
        logger = LoggerFactory.getLogger(loggerName);
        super.doStart();
    }
}