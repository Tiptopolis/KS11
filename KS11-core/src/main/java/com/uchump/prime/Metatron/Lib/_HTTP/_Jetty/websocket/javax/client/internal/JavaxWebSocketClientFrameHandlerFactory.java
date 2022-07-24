package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client.internal;


import javax.websocket.ClientEndpoint;
import javax.websocket.EndpointConfig;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.InvokerUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketFrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketFrameHandlerMetadata;

public class JavaxWebSocketClientFrameHandlerFactory extends JavaxWebSocketFrameHandlerFactory
{
    public JavaxWebSocketClientFrameHandlerFactory(JavaxWebSocketContainer container, InvokerUtils.ParamIdentifier paramIdentifier)
    {
        super(container, paramIdentifier);
    }

    public JavaxWebSocketClientFrameHandlerFactory(JavaxWebSocketContainer container)
    {
        super(container, InvokerUtils.PARAM_IDENTITY);
    }

    @Override
    public EndpointConfig newDefaultEndpointConfig(Class<?> endpointClass)
    {
        return new BasicClientEndpointConfig();
    }

    @Override
    public JavaxWebSocketFrameHandlerMetadata getMetadata(Class<?> endpointClass, EndpointConfig endpointConfig)
    {
        if (javax.websocket.Endpoint.class.isAssignableFrom(endpointClass))
            return createEndpointMetadata(endpointConfig);

        if (endpointClass.getAnnotation(ClientEndpoint.class) == null)
            return null;

        JavaxWebSocketFrameHandlerMetadata metadata = new JavaxWebSocketFrameHandlerMetadata(endpointConfig, components);
        return discoverJavaxFrameHandlerMetadata(endpointClass, metadata);
    }
}