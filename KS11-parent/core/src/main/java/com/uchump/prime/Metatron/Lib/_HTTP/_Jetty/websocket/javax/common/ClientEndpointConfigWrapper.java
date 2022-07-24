package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.util.List;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.Extension;

public class ClientEndpointConfigWrapper extends EndpointConfigWrapper implements ClientEndpointConfig
{
    private ClientEndpointConfig _endpointConfig;

    public ClientEndpointConfigWrapper()
    {
    }

    public ClientEndpointConfigWrapper(ClientEndpointConfig endpointConfig)
    {
        init(endpointConfig);
    }

    public void init(ClientEndpointConfig endpointConfig)
    {
        _endpointConfig = endpointConfig;
        super.init(endpointConfig);
    }

    @Override
    public List<String> getPreferredSubprotocols()
    {
        return _endpointConfig.getPreferredSubprotocols();
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