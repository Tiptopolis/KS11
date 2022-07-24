package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;


import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.websocket.Extension;
import javax.websocket.WebSocketContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.DecoratedObjectFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Dumpable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketExtensionRegistry;

public abstract class JavaxWebSocketContainer extends ContainerLifeCycle implements javax.websocket.WebSocketContainer, Dumpable
{
    private static final Logger LOG = LoggerFactory.getLogger(JavaxWebSocketContainer.class);
    private final List<JavaxWebSocketSessionListener> sessionListeners = new ArrayList<>();
    protected final SessionTracker sessionTracker = new SessionTracker();
    protected final Configuration.ConfigurationCustomizer defaultCustomizer = new Configuration.ConfigurationCustomizer();
    protected final WebSocketComponents components;

    public JavaxWebSocketContainer(WebSocketComponents components)
    {
        this.components = components;
        addSessionListener(sessionTracker);
        addBean(sessionTracker);
    }

    public abstract Executor getExecutor();

    protected abstract JavaxWebSocketFrameHandlerFactory getFrameHandlerFactory();

    public ByteBufferPool getBufferPool()
    {
        return components.getBufferPool();
    }

    public WebSocketExtensionRegistry getExtensionRegistry()
    {
        return components.getExtensionRegistry();
    }

    public DecoratedObjectFactory getObjectFactory()
    {
        return components.getObjectFactory();
    }

    public WebSocketComponents getWebSocketComponents()
    {
        return components;
    }

    public long getDefaultAsyncSendTimeout()
    {
        return defaultCustomizer.getWriteTimeout().toMillis();
    }

    @Override
    public int getDefaultMaxBinaryMessageBufferSize()
    {
        long max = defaultCustomizer.getMaxBinaryMessageSize();
        if (max > (long)Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return (int)max;
    }

    @Override
    public long getDefaultMaxSessionIdleTimeout()
    {
        return defaultCustomizer.getIdleTimeout().toMillis();
    }

    @Override
    public int getDefaultMaxTextMessageBufferSize()
    {
        long max = defaultCustomizer.getMaxTextMessageSize();
        if (max > (long)Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return (int)max;
    }

    @Override
    public void setAsyncSendTimeout(long ms)
    {
        defaultCustomizer.setWriteTimeout(Duration.ofMillis(ms));
    }

    @Override
    public void setDefaultMaxBinaryMessageBufferSize(int max)
    {
        defaultCustomizer.setMaxBinaryMessageSize(max);
    }

    @Override
    public void setDefaultMaxSessionIdleTimeout(long ms)
    {
        defaultCustomizer.setIdleTimeout(Duration.ofMillis(ms));
    }

    @Override
    public void setDefaultMaxTextMessageBufferSize(int max)
    {
        defaultCustomizer.setMaxTextMessageSize(max);
    }

    /**
     * {@inheritDoc}
     *
     * @see WebSocketContainer#getInstalledExtensions()
     * @since JSR356 v1.0
     */
    @Override
    public Set<Extension> getInstalledExtensions()
    {
        Set<Extension> ret = new HashSet<>();

        for (String name : getExtensionRegistry().getAvailableExtensionNames())
        {
            ret.add(new JavaxWebSocketExtension(name));
        }

        return ret;
    }

    /**
     * Used in {@link javax.websocket.Session#getOpenSessions()}
     *
     * @return the set of open sessions
     */
    public Set<javax.websocket.Session> getOpenSessions()
    {
        return sessionTracker.getSessions();
    }

    public JavaxWebSocketFrameHandler newFrameHandler(Object websocketPojo, UpgradeRequest upgradeRequest)
    {
        return getFrameHandlerFactory().newJavaxWebSocketFrameHandler(websocketPojo, upgradeRequest);
    }

    /**
     * Register a WebSocketSessionListener with the container
     *
     * @param listener the listener
     */
    public void addSessionListener(JavaxWebSocketSessionListener listener)
    {
        sessionListeners.add(listener);
    }

    /**
     * Remove a WebSocketSessionListener from the container
     *
     * @param listener the listener
     * @return true if listener was present and removed
     */
    public boolean removeSessionListener(JavaxWebSocketSessionListener listener)
    {
        return sessionListeners.remove(listener);
    }

    /**
     * Notify Session Listeners of events
     *
     * @param consumer the consumer to pass to each listener
     */
    public void notifySessionListeners(Consumer<JavaxWebSocketSessionListener> consumer)
    {
        for (JavaxWebSocketSessionListener listener : sessionListeners)
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
    public void dump(Appendable out, String indent) throws IOException
    {
        Dumpable.dumpObjects(out, indent, this, defaultCustomizer);
    }
}