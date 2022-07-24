package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.util.List;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

public class ServerEndpointConfigWrapper extends EndpointConfigWrapper implements ServerEndpointConfig
{
    private ServerEndpointConfig _endpointConfig;

    public ServerEndpointConfigWrapper()
    {
    }

    public ServerEndpointConfigWrapper(ServerEndpointConfig endpointConfig)
    {
        init(endpointConfig);
    }

    public void init(ServerEndpointConfig endpointConfig)
    {
        _endpointConfig = endpointConfig;
        super.init(endpointConfig);
    }

    @Override
    public Class<?> getEndpointClass()
    {
        return _endpointConfig.getEndpointClass();
    }

    @Override
    public String getPath()
    {
        return _endpointConfig.getPath();
    }

    @Override
    public List<String> getSubprotocols()
    {
        return _endpointConfig.getSubprotocols();
    }

    @Override
    public List<Extension> getExtensions()
    {
        return _endpointConfig.getExtensions();
    }

    @Override
    public Configurator getConfigurator()
    {
        return _endpointConfig.getConfigurator();
    }
}