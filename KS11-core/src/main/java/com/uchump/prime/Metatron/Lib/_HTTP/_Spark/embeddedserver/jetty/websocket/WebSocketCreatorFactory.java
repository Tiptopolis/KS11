package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty.websocket;

import static java.util.Objects.requireNonNull;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketCreator;

import spark.embeddedserver.jetty.websocket.WebSocketHandlerWrapper;

/**
 * Factory class to create {@link WebSocketCreator} implementations that
 * delegate to the given handler class.
 *
 * @author Ignasi Barrera
 */
public class WebSocketCreatorFactory {

    /**
     * Creates a {@link WebSocketCreator} that uses the given handler class/instance for
     * the WebSocket connections.
     *
     * @param handlerWrapper The wrapped handler to use to manage WebSocket connections.
     * @return The WebSocketCreator.
     */
    public static WebSocketCreator create(WebSocketHandlerWrapper handlerWrapper) {
        return new SparkWebSocketCreator(handlerWrapper.getHandler());
    }

    // Package protected to be visible to the unit tests
    static class SparkWebSocketCreator implements WebSocketCreator {
        private final Object handler;

        private SparkWebSocketCreator(Object handler) {
            this.handler = requireNonNull(handler, "handler cannot be null");
        }

        @Override
        public Object createWebSocket(ServletUpgradeRequest request,
                                      ServletUpgradeResponse response) {
            return handler;
        }

        Object getHandler() {
            return handler;
        }
    }
}