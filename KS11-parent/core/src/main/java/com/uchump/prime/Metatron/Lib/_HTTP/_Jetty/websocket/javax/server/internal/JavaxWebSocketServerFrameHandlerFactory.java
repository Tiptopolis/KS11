package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.internal;

import javax.websocket.EndpointConfig;
import javax.websocket.server.ServerEndpoint;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.UriTemplatePathSpec;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.FrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client.internal.JavaxWebSocketClientFrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketFrameHandlerMetadata;


public class JavaxWebSocketServerFrameHandlerFactory extends JavaxWebSocketClientFrameHandlerFactory implements FrameHandlerFactory
{
    public JavaxWebSocketServerFrameHandlerFactory(JavaxWebSocketContainer container)
    {
        super(container, new PathParamIdentifier());
    }

    @Override
    public JavaxWebSocketFrameHandlerMetadata getMetadata(Class<?> endpointClass, EndpointConfig endpointConfig)
    {
        if (javax.websocket.Endpoint.class.isAssignableFrom(endpointClass))
            return createEndpointMetadata(endpointConfig);

        ServerEndpoint anno = endpointClass.getAnnotation(ServerEndpoint.class);
        if (anno == null)
            return super.getMetadata(endpointClass, endpointConfig);

        UriTemplatePathSpec templatePathSpec = new UriTemplatePathSpec(anno.value());
        JavaxWebSocketFrameHandlerMetadata metadata = new JavaxWebSocketFrameHandlerMetadata(endpointConfig, components);
        metadata.setUriTemplatePathSpec(templatePathSpec);
        return discoverJavaxFrameHandlerMetadata(endpointClass, metadata);
    }

    @Override
    public FrameHandler newFrameHandler(Object websocketPojo, ServerUpgradeRequest upgradeRequest, ServerUpgradeResponse upgradeResponse)
    {
        return newJavaxWebSocketFrameHandler(websocketPojo, new JavaxServerUpgradeRequest(upgradeRequest));
    }
}