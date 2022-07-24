package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;

/**
 * <p>JMX Configuration</p>
 * <p>This configuration configures the WebAppContext server/system classes to
 * be able to see the org.eclipse.jetty.jmx package.   This class is defined
 * in the webapp package, as it implements the {@link Configuration} interface,
 * which is unknown to the jmx package.
 * </p>
 */
public class JmxConfiguration extends AbstractConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(JmxConfiguration.class);

    public JmxConfiguration()
    {
        addDependents(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class);
        protectAndExpose("org.eclipse.jetty.util.annotation", "org.eclipse.jetty.jmx.");
    }

    @Override
    public boolean isAvailable()
    {
        try
        {
            return Loader.loadClass("org.eclipse.jetty.jmx.ObjectMBean") != null;
        }
        catch (Throwable e)
        {
            LOG.trace("IGNORED", e);
            return false;
        }
    }
}