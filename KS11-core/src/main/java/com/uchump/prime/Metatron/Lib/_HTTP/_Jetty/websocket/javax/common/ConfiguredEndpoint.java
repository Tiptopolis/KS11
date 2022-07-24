package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import javax.websocket.EndpointConfig;

/**
 * Associate a JSR Endpoint with its optional {@link EndpointConfig}
 */
public class ConfiguredEndpoint
{
    /**
     * The instance of the Endpoint
     */
    private Object endpoint;
    /**
     * The optional instance specific configuration for the Endpoint
     */
    private final EndpointConfig config;

    public ConfiguredEndpoint(Object endpoint, EndpointConfig config)
    {
        this.endpoint = endpoint;
        this.config = config;
    }

    public EndpointConfig getConfig()
    {
        return config;
    }

    public Object getRawEndpoint()
    {
        return endpoint;
    }

    public void setRawEndpoint(Object rawEndpoint)
    {
        this.endpoint = rawEndpoint;
    }
}