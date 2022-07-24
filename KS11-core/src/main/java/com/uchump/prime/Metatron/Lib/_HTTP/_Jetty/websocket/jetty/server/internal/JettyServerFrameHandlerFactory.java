package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.internal;

import javax.servlet.ServletContext;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.FrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.JettyWebSocketFrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.JettyWebSocketFrameHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServerContainer;

public class JettyServerFrameHandlerFactory extends JettyWebSocketFrameHandlerFactory implements FrameHandlerFactory
{
    public static JettyServerFrameHandlerFactory getFactory(ServletContext servletContext)
    {
        JettyWebSocketServerContainer container = JettyWebSocketServerContainer.getContainer(servletContext);
        return (container == null) ? null : container.getBean(JettyServerFrameHandlerFactory.class);
    }

    public JettyServerFrameHandlerFactory(JettyWebSocketServerContainer container, WebSocketComponents components)
    {
        super(container, components);
    }

    @Override
    public FrameHandler newFrameHandler(Object websocketPojo, ServerUpgradeRequest upgradeRequest, ServerUpgradeResponse upgradeResponse)
    {
        JettyWebSocketFrameHandler frameHandler = super.newJettyFrameHandler(websocketPojo);
        frameHandler.setUpgradeRequest(new DelegatedServerUpgradeRequest(upgradeRequest));
        frameHandler.setUpgradeResponse(new DelegatedServerUpgradeResponse(upgradeResponse));
        return frameHandler;
    }
}