package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;

/**
 * <p>WebApp Configuration</p>
 * <p>This configuration configures the WebAppContext server/system classes to
 * be able to see default servlets.
 * </p>
 */
public class WebAppConfiguration extends AbstractConfiguration
{
    public WebAppConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class);
        addDependents(JettyWebXmlConfiguration.class);
        protectAndExpose(
            "org.eclipse.jetty.servlet.StatisticsServlet",
            "org.eclipse.jetty.servlet.DefaultServlet",
            "org.eclipse.jetty.servlet.NoJspServlet"
        );
        expose("org.eclipse.jetty.servlet.listener.");
    }
}