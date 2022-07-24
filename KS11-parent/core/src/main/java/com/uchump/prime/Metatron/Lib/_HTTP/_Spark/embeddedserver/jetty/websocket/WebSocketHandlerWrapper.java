package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty.websocket;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;

/**
 * A wrapper for web socket handler classes/instances.
 */
public interface WebSocketHandlerWrapper {
    
    /**
     * Gets the actual handler - if necessary, instantiating an object.
     * 
     * @return The handler instance.
     */
    Object getHandler();
    
    static void validateHandlerClass(Class<?> handlerClass) {
        boolean valid = WebSocketListener.class.isAssignableFrom(handlerClass)
                || handlerClass.isAnnotationPresent(WebSocket.class);
        if (!valid) {
            throw new IllegalArgumentException(
                    "WebSocket handler must implement 'WebSocketListener' or be annotated as '@WebSocket'");
        }
    }

}