package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client.internal;

import java.util.List;
import java.util.Map;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.HandshakeResponse;

public class EmptyConfigurator extends ClientEndpointConfig.Configurator
{
    public static final EmptyConfigurator INSTANCE = new EmptyConfigurator();

    @Override
    public void afterResponse(HandshakeResponse hr)
    {
        // do nothing
    }

    @Override
    public void beforeRequest(Map<String, List<String>> headers)
    {
        // do nothing
    }
}