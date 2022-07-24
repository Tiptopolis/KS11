package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client.internal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.ClientEndpointConfigWrapper;

public class BasicClientEndpointConfig extends ClientEndpointConfigWrapper
{
    public BasicClientEndpointConfig()
    {
        init(Builder.create().configurator(EmptyConfigurator.INSTANCE).build());
    }
}