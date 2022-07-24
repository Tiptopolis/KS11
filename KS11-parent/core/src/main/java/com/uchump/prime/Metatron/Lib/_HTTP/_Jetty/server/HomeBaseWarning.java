package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.StringUtil;

/**
 * Display an optional Warning Message if the {jetty.home} and {jetty.base} are the same directory.
 * <p>
 * This is to warn about not recommended approach to setting up the Jetty Distribution.
 */
public class HomeBaseWarning
{
    private static final Logger LOG = LoggerFactory.getLogger(HomeBaseWarning.class);

    public HomeBaseWarning()
    {
        boolean showWarn = false;

        String home = System.getProperty("jetty.home");
        String base = System.getProperty("jetty.base");

        if (StringUtil.isBlank(base))
        {
            // no base defined? then we are likely running
            // via direct command line.
            return;
        }

        Path homePath = new File(home).toPath();
        Path basePath = new File(base).toPath();

        try
        {
            showWarn = Files.isSameFile(homePath, basePath);
        }
        catch (IOException e)
        {
            LOG.trace("IGNORED", e);
            // Can't definitively determine this state
            return;
        }

        if (showWarn)
        {
            StringBuilder warn = new StringBuilder();
            warn.append("This instance of Jetty is not running from a separate {jetty.base} directory");
            warn.append(", this is not recommended.  See documentation at https://www.eclipse.org/jetty/documentation/current/startup.html");
            LOG.warn("{}", warn.toString());
        }
    }
}