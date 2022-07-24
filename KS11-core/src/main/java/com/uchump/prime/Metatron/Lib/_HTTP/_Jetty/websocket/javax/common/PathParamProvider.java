package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.util.Map;

/**
 * Optional interface for custom {@link javax.websocket.EndpointConfig} implementations
 * in Jetty to expose Path Param values used during the {@link JavaxWebSocketFrameHandler}
 * resolution of methods.
 * <p>
 * Mostly a feature of the JSR356 Server implementation and its {@code &#064;javax.websocket.server.PathParam} annotation.
 * </p>
 */
public interface PathParamProvider
{
    Map<String, String> getPathParams();
}