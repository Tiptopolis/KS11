package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.AbstractConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.FragmentConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebXmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Websocket Configuration</p>
 * <p>This configuration configures the WebAppContext server/system classes to
 * be able to see the {@code org.eclipse.jetty.websocket.api}, {@code org.eclipse.jetty.websocket.server} and
 * {@code org.eclipse.jetty.websocket.servlet} packages.</p>
 */
public class JettyWebSocketConfiguration extends AbstractConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(JettyWebSocketConfiguration.class);

    public JettyWebSocketConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class, FragmentConfiguration.class);
        addDependents("org.eclipse.jetty.annotations.AnnotationConfiguration", WebAppConfiguration.class.getName());

        protectAndExpose("org.eclipse.jetty.websocket.api.");
        protectAndExpose("org.eclipse.jetty.websocket.server.");
        protectAndExpose("org.eclipse.jetty.websocket.servlet."); // For WebSocketUpgradeFilter
        hide("org.eclipse.jetty.server.internal.");
        hide("org.eclipse.jetty.server.config.");
    }
}