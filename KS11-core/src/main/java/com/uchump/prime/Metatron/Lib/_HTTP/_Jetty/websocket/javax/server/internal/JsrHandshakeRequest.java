package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.internal;


import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import javax.websocket.server.HandshakeRequest;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.PathSpec;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeRequest;

public class JsrHandshakeRequest implements HandshakeRequest
{
    private final ServerUpgradeRequest delegate;

    public JsrHandshakeRequest(ServerUpgradeRequest req)
    {
        this.delegate = req;
    }

    @Override
    public Map<String, List<String>> getHeaders()
    {
        return delegate.getHeadersMap();
    }

    @Override
    public Object getHttpSession()
    {
        return delegate.getSession();
    }

    @Override
    public Map<String, List<String>> getParameterMap()
    {
        return delegate.getParameterMap();
    }

    @Override
    public String getQueryString()
    {
        return delegate.getQueryString();
    }

    public PathSpec getRequestPathSpec()
    {
        return (PathSpec)delegate.getServletAttribute(PathSpec.class.getName());
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getPathParams()
    {
        return (Map<String, String>)delegate.getServletAttribute(JavaxWebSocketServerContainer.PATH_PARAM_ATTRIBUTE);
    }

    @Override
    public URI getRequestURI()
    {
        return delegate.getRequestURI();
    }

    @Override
    public Principal getUserPrincipal()
    {
        return delegate.getUserPrincipal();
    }

    @Override
    public boolean isUserInRole(String role)
    {
        return delegate.isUserInRole(role);
    }
}