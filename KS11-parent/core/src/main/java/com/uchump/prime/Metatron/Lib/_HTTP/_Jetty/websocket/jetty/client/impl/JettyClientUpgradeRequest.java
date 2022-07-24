package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.impl;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.JettyWebSocketFrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.JettyWebSocketFrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.CoreClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.WebSocketCoreClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;

public class JettyClientUpgradeRequest extends CoreClientUpgradeRequest
{
    private final JettyWebSocketFrameHandler frameHandler;

    public JettyClientUpgradeRequest(WebSocketCoreClient coreClient, ClientUpgradeRequest request, URI requestURI, JettyWebSocketFrameHandlerFactory frameHandlerFactory,
                                     Object websocketPojo)
    {
        super(coreClient, requestURI);

        if (request != null)
        {
            // Copy request details into actual request
            headers(fields -> request.getHeaders().forEach(fields::put));

            // Copy manually created Cookies into place
            headers(fields -> request.getCookies().forEach(cookie -> fields.add(HttpHeader.COOKIE, cookie.toString())));

            setSubProtocols(request.getSubProtocols());
            setExtensions(request.getExtensions().stream()
                .map(c -> new ExtensionConfig(c.getName(), c.getParameters()))
                .collect(Collectors.toList()));

            timeout(request.getTimeout(), TimeUnit.MILLISECONDS);
        }

        frameHandler = frameHandlerFactory.newJettyFrameHandler(websocketPojo);
    }

    @Override
    public void upgrade(HttpResponse response, EndPoint endPoint)
    {
        frameHandler.setUpgradeRequest(new DelegatedJettyClientUpgradeRequest(this));
        frameHandler.setUpgradeResponse(new DelegatedJettyClientUpgradeResponse(response));
        super.upgrade(response, endPoint);
    }

    @Override
    public FrameHandler getFrameHandler()
    {
        return frameHandler;
    }
}