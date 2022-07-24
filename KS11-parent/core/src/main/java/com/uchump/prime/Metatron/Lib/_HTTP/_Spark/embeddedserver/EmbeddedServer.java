package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver;

import java.util.Map;
import java.util.Optional;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty.websocket.WebSocketHandlerWrapper;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.ssl.SslStores;

/**
 * Represents an embedded server that can be used in Spark. (this is currently Jetty by default).
 */
public interface EmbeddedServer {

    /**
     * Ignites the embedded server, listening on the specified port, running SSL secured with the specified keystore
     * and truststore.  If truststore is null, keystore is reused.
     *
     * @param host                    The address to listen on
     * @param port                    - the port
     * @param sslStores               - The SSL sslStores.
     * @param maxThreads              - max nbr of threads.
     * @param minThreads              - min nbr of threads.
     * @return The port number the server was launched on.
     */
    int ignite(String host,
                   int port,
                   SslStores sslStores,
                   int maxThreads,
                   int minThreads,
                   int threadIdleTimeoutMillis) throws Exception;


    /**
     * Must be called before ignite()
     *
     * Must be it's own default method to maintain backwards compatibility. Move to ignite method in 3.0.
     */
    default void trustForwardHeaders(boolean trust) {

    }

    /**
     * Configures the web sockets for the embedded server.
     *
     * @param webSocketHandlers          - web socket handlers.
     * @param webSocketIdleTimeoutMillis - Optional WebSocket idle timeout (ms).
     */
    default void configureWebSockets(Map<String, WebSocketHandlerWrapper> webSocketHandlers,
                                     Optional<Long> webSocketIdleTimeoutMillis) {

        NotSupportedException.raise(getClass().getSimpleName(), "Web Sockets");
    }

    /**
     * Joins the embedded server thread(s).
     */
    void join() throws InterruptedException;

    /**
     * Extinguish the embedded server.
     */
    void extinguish();

    /**
     *
     * @return The approximate number of currently active threads
     */
    int activeThreadCount();
}