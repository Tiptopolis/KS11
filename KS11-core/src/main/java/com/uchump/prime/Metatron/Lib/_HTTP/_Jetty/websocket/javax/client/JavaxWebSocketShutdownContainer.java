package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client;

import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.LifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client.internal.JavaxWebSocketClientContainer;

/**
 * <p>This manages the LifeCycle of {@link javax.websocket.WebSocketContainer} instances that are created with
 * {@link javax.websocket.ContainerProvider}, if this code is being run from another ServletContainer, or if run inside a
 * Jetty Server with the WebSocket client classes provided by the webapp.</p>
 *
 * <p>This mechanism will not work if run with embedded Jetty or if the WebSocket client classes are provided by the server.
 * In this case then the client {@link javax.websocket.WebSocketContainer} will register itself to be automatically shutdown
 * with the Jetty {@code ContextHandler}.</p>
 */
public class JavaxWebSocketShutdownContainer extends ContainerLifeCycle implements ServletContainerInitializer, ServletContextListener
{
    private static final Logger LOG = LoggerFactory.getLogger(JavaxWebSocketShutdownContainer.class);

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException
    {
        JavaxWebSocketClientContainer.setShutdownContainer(this);
        ctx.addListener(this);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("contextInitialized({}) {}", sce, this);
        LifeCycle.start(this);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("contextDestroyed({}) {}", sce, this);

        LifeCycle.stop(this);
        removeBeans();
        JavaxWebSocketClientContainer.setShutdownContainer(null);
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x{%s, size=%s}", getClass().getSimpleName(), hashCode(), getState(), getBeans().size());
    }
}