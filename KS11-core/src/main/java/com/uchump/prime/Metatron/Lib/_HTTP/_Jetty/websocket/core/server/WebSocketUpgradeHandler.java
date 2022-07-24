package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.PathSpec;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.ServletPathSpec;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.HandlerWrapper;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;

public class WebSocketUpgradeHandler extends HandlerWrapper
{
    private final WebSocketMappings mappings;
    private final Configuration.ConfigurationCustomizer customizer = new Configuration.ConfigurationCustomizer();

    public WebSocketUpgradeHandler()
    {
        this(new WebSocketComponents());
    }

    public WebSocketUpgradeHandler(WebSocketComponents components)
    {
        this.mappings = new WebSocketMappings(components);
    }

    public void addMapping(String pathSpec, WebSocketNegotiator negotiator)
    {
        mappings.addMapping(new ServletPathSpec(pathSpec), negotiator);
    }

    public void addMapping(PathSpec pathSpec, WebSocketNegotiator negotiator)
    {
        mappings.addMapping(pathSpec, negotiator);
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if (mappings.upgrade(request, response, customizer))
            return;

        if (!baseRequest.isHandled())
            super.handle(target, baseRequest, request, response);
    }
}