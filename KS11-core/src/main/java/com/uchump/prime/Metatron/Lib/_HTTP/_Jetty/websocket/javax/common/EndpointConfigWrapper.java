package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public abstract class EndpointConfigWrapper implements EndpointConfig, PathParamProvider
{
    private EndpointConfig _endpointConfig;
    private Map<String, String> _pathParameters;

    public EndpointConfigWrapper()
    {
    }

    public EndpointConfigWrapper(EndpointConfig endpointConfig)
    {
        init(endpointConfig);
    }

    public void init(EndpointConfig endpointConfig)
    {
        _endpointConfig = endpointConfig;

        if (endpointConfig instanceof PathParamProvider)
            _pathParameters = ((PathParamProvider)endpointConfig).getPathParams();
        else
            _pathParameters = Collections.emptyMap();
    }

    @Override
    public List<Class<? extends Encoder>> getEncoders()
    {
        return _endpointConfig.getEncoders();
    }

    @Override
    public List<Class<? extends Decoder>> getDecoders()
    {
        return _endpointConfig.getDecoders();
    }

    @Override
    public Map<String, Object> getUserProperties()
    {
        return _endpointConfig.getUserProperties();
    }

    @Override
    public Map<String, String> getPathParams()
    {
        return _pathParameters;
    }
}