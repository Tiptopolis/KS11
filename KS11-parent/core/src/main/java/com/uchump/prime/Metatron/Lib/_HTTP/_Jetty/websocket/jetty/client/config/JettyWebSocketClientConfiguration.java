package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.config;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.AbstractConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.FragmentConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebXmlConfiguration;

/**
 * <p>Websocket Configuration</p>
 * <p>This configuration configures the WebAppContext server/system classes to
 * be able to see the {@code org.eclipse.jetty.websocket.client} package.</p>
 */
public class JettyWebSocketClientConfiguration extends AbstractConfiguration
{
    public JettyWebSocketClientConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class, FragmentConfiguration.class);
        addDependents("org.eclipse.jetty.annotations.AnnotationConfiguration", WebAppConfiguration.class.getName());

        protectAndExpose("org.eclipse.jetty.websocket.api.");
        protectAndExpose("org.eclipse.jetty.websocket.client.");
        hide("org.eclipse.jetty.websocket.client.impl.");
        hide("org.eclipse.jetty.websocket.client.config.");
    }
}