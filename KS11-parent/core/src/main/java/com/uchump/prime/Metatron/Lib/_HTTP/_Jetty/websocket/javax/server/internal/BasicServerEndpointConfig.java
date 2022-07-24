package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.internal;

import javax.websocket.server.ServerEndpointConfig;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.ServerEndpointConfigWrapper;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.config.ContainerDefaultConfigurator;

public class BasicServerEndpointConfig extends ServerEndpointConfigWrapper
{
    private final String _path;

    public BasicServerEndpointConfig(Class<?> endpointClass, String path)
    {
        ServerEndpointConfig config = Builder.create(endpointClass, "/")
            .configurator(new ContainerDefaultConfigurator())
            .build();
        _path = path;
        init(config);
    }

    @Override
    public String getPath()
    {
        if (_path == null)
            throw new RuntimeException("Path is undefined");

        return _path;
    }
}