package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;

/**
 * <p>Jetty Servlets Configuration</p>
 * <p>This configuration configures the WebAppContext server/system classes to
 * expose the jetty utility servlets if they are on the server classpath.
 * </p>
 */
public class ServletsConfiguration extends AbstractConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(ServletsConfiguration.class);

    public ServletsConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class, WebAppConfiguration.class);
        addDependents(JettyWebXmlConfiguration.class);
        protectAndExpose();
        protect("org.eclipse.jetty.servlets.PushCacheFilter", //must be loaded by container classpath
            "org.eclipse.jetty.servlets.PushSessionCacheFilter" //must be loaded by container classpath
        );
        expose("org.eclipse.jetty.servlets."); // don't hide jetty servlets
    }

    @Override
    public boolean isAvailable()
    {
        try
        {
            return Loader.loadClass("org.eclipse.jetty.servlets.PushCacheFilter") != null;
        }
        catch (Throwable e)
        {
            LOG.trace("IGNORED", e);
            return false;
        }
    }
}