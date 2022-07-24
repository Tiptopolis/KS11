package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.PathSpec;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Dumpable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.LifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketBehavior;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPolicy;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketSessionListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.WebSocketException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.ReflectUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.Handshaker;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketCreator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketMappings;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketServerComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.SessionTracker;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.internal.DelegatedServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.internal.DelegatedServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.internal.JettyServerFrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.servlet.WebSocketUpgradeFilter;

public class JettyWebSocketServerContainer extends ContainerLifeCycle implements WebSocketContainer, WebSocketPolicy, LifeCycle.Listener
{
    public static final String JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE = WebSocketContainer.class.getName();

    public static JettyWebSocketServerContainer getContainer(ServletContext servletContext)
    {
        return (JettyWebSocketServerContainer)servletContext.getAttribute(JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE);
    }

    public static JettyWebSocketServerContainer ensureContainer(ServletContext servletContext)
    {
        ServletContextHandler contextHandler = ServletContextHandler.getServletContextHandler(servletContext, "Javax Websocket");
        if (contextHandler.getServer() == null)
            throw new IllegalStateException("Server has not been set on the ServletContextHandler");

        // If we find a container in the servlet context return it.
        JettyWebSocketServerContainer containerFromServletContext = getContainer(servletContext);
        if (containerFromServletContext != null)
            return containerFromServletContext;

        // Find Pre-Existing executor.
        Executor executor = (Executor)servletContext.getAttribute("org.eclipse.jetty.server.Executor");
        if (executor == null)
            executor = contextHandler.getServer().getThreadPool();

        // Create the Jetty ServerContainer implementation.
        WebSocketMappings mappings = WebSocketMappings.ensureMappings(servletContext);
        WebSocketComponents components = WebSocketServerComponents.getWebSocketComponents(servletContext);
        JettyWebSocketServerContainer container = new JettyWebSocketServerContainer(contextHandler, mappings, components, executor);

        // Manage the lifecycle of the Container.
        contextHandler.addManaged(container);
        contextHandler.addEventListener(container);
        contextHandler.addEventListener(new LifeCycle.Listener()
        {
            @Override
            public void lifeCycleStopping(LifeCycle event)
            {
                contextHandler.getServletContext().removeAttribute(JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE);
                contextHandler.removeBean(container);
                contextHandler.removeEventListener(container);
                contextHandler.removeEventListener(this);
            }

            @Override
            public String toString()
            {
                return String.format("%sCleanupListener", JettyWebSocketServerContainer.class.getSimpleName());
            }
        });

        servletContext.setAttribute(JETTY_WEBSOCKET_CONTAINER_ATTRIBUTE, container);
        return container;
    }

    private static final Logger LOG = LoggerFactory.getLogger(JettyWebSocketServerContainer.class);

    private final ServletContextHandler contextHandler;
    private final WebSocketMappings webSocketMappings;
    private final WebSocketComponents components;
    private final JettyServerFrameHandlerFactory frameHandlerFactory;
    private final Executor executor;
    private final Configuration.ConfigurationCustomizer customizer = new Configuration.ConfigurationCustomizer();

    private final List<WebSocketSessionListener> sessionListeners = new ArrayList<>();
    private final SessionTracker sessionTracker = new SessionTracker();

    /**
     * Main entry point for {@link JettyWebSocketServletContainerInitializer}.
     *
     * @param webSocketMappings the {@link WebSocketMappings} that this container belongs to
     * @param executor the {@link Executor} to use
     */
    JettyWebSocketServerContainer(ServletContextHandler contextHandler, WebSocketMappings webSocketMappings, WebSocketComponents components, Executor executor)
    {
        this.contextHandler = contextHandler;
        this.webSocketMappings = webSocketMappings;
        this.components = components;
        this.executor = executor;
        this.frameHandlerFactory = new JettyServerFrameHandlerFactory(this, components);
        addBean(frameHandlerFactory);

        addSessionListener(sessionTracker);
        addBean(sessionTracker);
    }

    public void addMapping(String pathSpec, JettyWebSocketCreator creator)
    {
        PathSpec ps = WebSocketMappings.parsePathSpec(pathSpec);
        if (webSocketMappings.getWebSocketNegotiator(ps) != null)
            throw new WebSocketException("Duplicate WebSocket Mapping for PathSpec");

        WebSocketUpgradeFilter.ensureFilter(contextHandler.getServletContext());
        WebSocketCreator coreCreator = (req, resp) -> creator.createWebSocket(new DelegatedServerUpgradeRequest(req), new DelegatedServerUpgradeResponse(resp));
        webSocketMappings.addMapping(ps, coreCreator, frameHandlerFactory, customizer);
    }

    public void addMapping(String pathSpec, final Class<?> endpointClass)
    {
        if (!ReflectUtils.isDefaultConstructable(endpointClass))
            throw new IllegalArgumentException("Cannot access default constructor for the class: " + endpointClass.getName());

        addMapping(pathSpec, (req, resp) ->
        {
            try
            {
                return endpointClass.getDeclaredConstructor().newInstance();
            }
            catch (Exception e)
            {
                throw new com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions.WebSocketException("Unable to create instance of " + endpointClass.getName(), e);
            }
        });
    }

    /**
     * An immediate programmatic WebSocket upgrade that does not register a mapping or create a {@link WebSocketUpgradeFilter}.
     * @param creator the WebSocketCreator to use.
     * @param request the HttpServletRequest.
     * @param response the HttpServletResponse.
     * @return true if the connection was successfully upgraded to WebSocket.
     * @throws IOException if an I/O error occurs.
     */
    public boolean upgrade(JettyWebSocketCreator creator, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        WebSocketCreator coreCreator = (req, resp) -> creator.createWebSocket(new DelegatedServerUpgradeRequest(req), new DelegatedServerUpgradeResponse(resp));
        WebSocketNegotiator negotiator = WebSocketNegotiator.from(coreCreator, frameHandlerFactory, customizer);

        Handshaker handshaker = webSocketMappings.getHandshaker();
        return handshaker.upgradeRequest(negotiator, request, response, components, null);
    }

    @Override
    public Executor getExecutor()
    {
        return this.executor;
    }

    @Override
    public void addSessionListener(WebSocketSessionListener listener)
    {
        sessionListeners.add(listener);
    }

    @Override
    public boolean removeSessionListener(WebSocketSessionListener listener)
    {
        return sessionListeners.remove(listener);
    }

    @Override
    public void notifySessionListeners(Consumer<WebSocketSessionListener> consumer)
    {
        for (WebSocketSessionListener listener : sessionListeners)
        {
            try
            {
                consumer.accept(listener);
            }
            catch (Throwable x)
            {
                LOG.info("Exception while invoking listener {}", listener, x);
            }
        }
    }

    @Override
    public Collection<Session> getOpenSessions()
    {
        return sessionTracker.getSessions();
    }

    @Override
    public WebSocketBehavior getBehavior()
    {
        return WebSocketBehavior.SERVER;
    }

    @Override
    public Duration getIdleTimeout()
    {
        return customizer.getIdleTimeout();
    }

    @Override
    public int getInputBufferSize()
    {
        return customizer.getInputBufferSize();
    }

    @Override
    public int getOutputBufferSize()
    {
        return customizer.getOutputBufferSize();
    }

    @Override
    public long getMaxBinaryMessageSize()
    {
        return customizer.getMaxBinaryMessageSize();
    }

    @Override
    public long getMaxTextMessageSize()
    {
        return customizer.getMaxTextMessageSize();
    }

    @Override
    public long getMaxFrameSize()
    {
        return customizer.getMaxFrameSize();
    }

    @Override
    public boolean isAutoFragment()
    {
        return customizer.isAutoFragment();
    }

    @Override
    public void setIdleTimeout(Duration duration)
    {
        customizer.setIdleTimeout(duration);
    }

    @Override
    public void setInputBufferSize(int size)
    {
        customizer.setInputBufferSize(size);
    }

    @Override
    public void setOutputBufferSize(int size)
    {
        customizer.setOutputBufferSize(size);
    }

    @Override
    public void setMaxBinaryMessageSize(long size)
    {
        customizer.setMaxBinaryMessageSize(size);
    }

    @Override
    public void setMaxTextMessageSize(long size)
    {
        customizer.setMaxTextMessageSize(size);
    }

    @Override
    public void setMaxFrameSize(long maxFrameSize)
    {
        customizer.setMaxFrameSize(maxFrameSize);
    }

    @Override
    public void setAutoFragment(boolean autoFragment)
    {
        customizer.setAutoFragment(autoFragment);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException
    {
        Dumpable.dumpObjects(out, indent, this, customizer);
    }
}