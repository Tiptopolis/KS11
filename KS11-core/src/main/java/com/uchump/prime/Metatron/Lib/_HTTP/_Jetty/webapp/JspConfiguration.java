package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;

/**
 * <p>JSP Configuration</p>
 * <p>This configuration configures the WebAppContext server/system classes to
 * be able to see the org.eclipse.jetty.jsp and org.eclipse.jetty.apache packages.
 * This class is defined in the webapp package, as it implements the {@link Configuration} interface,
 * which is unknown to the jsp package.
 * </p>
 */
public class JspConfiguration extends AbstractConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(JspConfiguration.class);

    public JspConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class, FragmentConfiguration.class);
        addDependents(WebAppConfiguration.class);
        protectAndExpose("org.eclipse.jetty.jsp.");
        expose("org.eclipse.jetty.apache.");
        hide("org.eclipse.jdt.");
    }

    @Override
    public boolean isAvailable()
    {
        try
        {
            return Loader.loadClass("org.eclipse.jetty.jsp.JettyJspServlet") != null;
        }
        catch (Throwable e)
        {
            LOG.trace("IGNORED", e);
            return false;
        }
    }
}