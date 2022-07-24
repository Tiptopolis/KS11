package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;

/**
 * <p>JASPI Configuration</p>
 * <p>This configuration configures the WebAppContext server/system classes to
 * not be able to see the {@code javax.security.auth.message} package.</p>
 */
public class JaspiConfiguration extends AbstractConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(JaspiConfiguration.class);

    public JaspiConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class, FragmentConfiguration.class);
        addDependents(WebAppConfiguration.class);

        hide("javax.security.auth.message.");
    }

    @Override
    public boolean isAvailable()
    {
        try
        {
            return Loader.loadClass("org.eclipse.jetty.security.jaspi.JaspiAuthenticator") != null;
        }
        catch (Throwable e)
        {
            LOG.trace("IGNORED", e);
            return false;
        }
    }
}