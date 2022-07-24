package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server;

import java.util.Set;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketBehavior;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPolicy;

public interface JettyWebSocketServletFactory extends WebSocketPolicy
{
    /**
     * Add a WebSocket mapping to a provided {@link JettyWebSocketCreator}.
     * <p>
     * If mapping is added before this configuration is started, then it is persisted through
     * stop/start of this configuration's lifecycle.  Otherwise it will be removed when
     * this configuration is stopped.
     * </p>
     *
     * @param pathSpec the pathspec to respond on
     * @param creator the WebSocketCreator to use
     * @since 10.0
     */
    void addMapping(String pathSpec, JettyWebSocketCreator creator);

    /**
     * Add a WebSocket mapping at PathSpec "/" for a creator which creates the endpointClass
     *
     * @param endpointClass the WebSocket class to use
     */
    void register(Class<?> endpointClass);

    /**
     * Add a WebSocket mapping at PathSpec "/" for a creator
     *
     * @param creator the WebSocketCreator to use
     */
    void setCreator(JettyWebSocketCreator creator);

    /**
     * Returns the creator for the given path spec.
     *
     * @param pathSpec the pathspec to respond on
     * @return the websocket creator if path spec exists, or null
     */
    JettyWebSocketCreator getMapping(String pathSpec);

    /**
     * Removes the mapping based on the given path spec.
     *
     * @param pathSpec the pathspec to respond on
     * @return true if underlying mapping were altered, false otherwise
     */
    boolean removeMapping(String pathSpec);

    /**
     * Get the names of all available WebSocket Extensions.
     * @return a set the available extension names.
     */
    Set<String> getAvailableExtensionNames();

    @Override
    default WebSocketBehavior getBehavior()
    {
        return WebSocketBehavior.SERVER;
    }
}