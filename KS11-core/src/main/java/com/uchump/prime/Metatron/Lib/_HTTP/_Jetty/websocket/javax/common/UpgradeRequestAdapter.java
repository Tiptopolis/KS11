package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.net.URI;
import java.security.Principal;

public class UpgradeRequestAdapter implements UpgradeRequest
{
    private final URI requestURI;
    private final String pathInContext;

    public UpgradeRequestAdapter()
    {
        /* anonymous, no requestURI, upgrade request */
        this(null, null);
    }

    public UpgradeRequestAdapter(URI uri, String pathInContext)
    {
        this.requestURI = uri;
        this.pathInContext = pathInContext;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return null;
    }

    @Override
    public URI getRequestURI()
    {
        return requestURI;
    }

    @Override
    public String getPathInContext()
    {
        return pathInContext;
    }
}