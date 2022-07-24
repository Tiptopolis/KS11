package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.internal;

import java.net.URI;
import java.security.Principal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.UpgradeRequest;

public class JavaxServerUpgradeRequest implements UpgradeRequest
{
    private final ServerUpgradeRequest servletRequest;

    public JavaxServerUpgradeRequest(ServerUpgradeRequest servletRequest)
    {
        this.servletRequest = servletRequest;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return servletRequest.getUserPrincipal();
    }

    @Override
    public URI getRequestURI()
    {
        return servletRequest.getRequestURI();
    }

    @Override
    public String getPathInContext()
    {
        return servletRequest.getPathInContext();
    }
}