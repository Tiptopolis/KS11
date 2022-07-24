package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Generic interface to the Container (server or client)
 */
public interface WebSocketContainer
{
    /**
     * The Container provided Executor.
     */
    Executor getExecutor();

    /**
     * Get the collection of open Sessions being tracked by this container
     *
     * @return the collection of open sessions
     */
    Collection<Session> getOpenSessions();

    /**
     * Register a WebSocketSessionListener with the container
     *
     * @param listener the listener
     */
    void addSessionListener(WebSocketSessionListener listener);

    /**
     * Remove a WebSocketSessionListener from the container
     *
     * @param listener the listener
     * @return true if listener was present and removed
     */
    boolean removeSessionListener(WebSocketSessionListener listener);

    /**
     * Notify the Session Listeners of an event.
     *
     * @param consumer the consumer to call for each tracked listener
     */
    void notifySessionListeners(Consumer<WebSocketSessionListener> consumer);
}