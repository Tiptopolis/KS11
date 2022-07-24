package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;

/**
 * <p>JNDI Configuration</p>
 * <p>This configuration configures the WebAppContext system/server classes to
 * be able to see the org.eclipse.jetty.jaas package.
 * This class is defined in the webapp package, as it implements the {@link Configuration} interface,
 * which is unknown to the jndi package.
 * </p>
 */
public class JndiConfiguration extends AbstractConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(JndiConfiguration.class);

    public JndiConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class, FragmentConfiguration.class);
        addDependents(WebAppConfiguration.class);
        protectAndExpose("org.eclipse.jetty.jndi.");
    }

    @Override
    public boolean isAvailable()
    {
        try
        {
            return Loader.loadClass("org.eclipse.jetty.jndi.InitialContextFactory") != null;
        }
        catch (Throwable e)
        {
            LOG.trace("IGNORED", e);
            return false;
        }
    }
}