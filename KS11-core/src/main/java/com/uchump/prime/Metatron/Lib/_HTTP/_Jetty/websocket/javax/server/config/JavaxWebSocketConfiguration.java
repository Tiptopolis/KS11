package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.config;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.AbstractConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.FragmentConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebXmlConfiguration;


/**
 * <p>Websocket Configuration</p>
 * <p>This configuration configures the WebAppContext server/system classes to
 * be able to see the {@code org.eclipse.jetty.websocket.javax} packages.</p>
 */
public class JavaxWebSocketConfiguration extends AbstractConfiguration
{
    public JavaxWebSocketConfiguration()
    {
    	;
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, WebInfConfiguration.class, FragmentConfiguration.class);
        addDependents("org.eclipse.jetty.annotations.AnnotationConfiguration", WebAppConfiguration.class.getName());

        protectAndExpose("org.eclipse.jetty.websocket.servlet."); // For WebSocketUpgradeFilter
        protectAndExpose("org.eclipse.jetty.websocket.javax.server.config.");
        protectAndExpose("org.eclipse.jetty.websocket.javax.client.JavaxWebSocketClientContainerProvider");
        protectAndExpose("org.eclipse.jetty.websocket.javax.client.JavaxWebSocketShutdownContainer");
        hide("org.eclipse.jetty.websocket.javax.server.internal");
    }
}