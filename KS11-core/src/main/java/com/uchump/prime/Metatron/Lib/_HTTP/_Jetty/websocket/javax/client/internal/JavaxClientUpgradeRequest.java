package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client.internal;


import java.net.URI;
import java.security.Principal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.CoreClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.WebSocketCoreClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketFrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.UpgradeRequest;

public class JavaxClientUpgradeRequest extends CoreClientUpgradeRequest implements UpgradeRequest
{
    private final JavaxWebSocketFrameHandler frameHandler;

    public JavaxClientUpgradeRequest(JavaxWebSocketClientContainer clientContainer, WebSocketCoreClient coreClient, URI requestURI, Object websocketPojo)
    {
        super(coreClient, requestURI);
        frameHandler = clientContainer.newFrameHandler(websocketPojo, this);
    }

    @Override
    public FrameHandler getFrameHandler()
    {
        return frameHandler;
    }

    @Override
    public Principal getUserPrincipal()
    {
        // User Principal not available from Client API
        return null;
    }

    @Override
    public URI getRequestURI()
    {
        return getURI();
    }

    @Override
    public String getPathInContext()
    {
        throw new UnsupportedOperationException();
    }
}