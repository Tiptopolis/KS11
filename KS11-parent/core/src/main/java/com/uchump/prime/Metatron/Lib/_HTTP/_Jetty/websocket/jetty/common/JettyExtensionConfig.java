package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common;


import java.util.Map;
import java.util.Set;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;

/**
 * Represents an Extension Configuration, as seen during the connection Handshake process.
 */
public class JettyExtensionConfig implements com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.ExtensionConfig
{

    private final ExtensionConfig config;

    /**
     * Copy constructor
     *
     * @param copy the extension config to copy
     */
    public JettyExtensionConfig(JettyExtensionConfig copy)
    {
        this(copy.config);
    }

    public JettyExtensionConfig(ExtensionConfig config)
    {
        this.config = config;
    }

    public JettyExtensionConfig(String parameterizedName)
    {
        this.config = new ExtensionConfig(parameterizedName);
    }

    public JettyExtensionConfig(String name, Map<String, String> parameters)
    {
        this.config = new ExtensionConfig(name, parameters);
    }

    public ExtensionConfig getCoreConfig()
    {
        return config;
    }

    @Override
    public String getName()
    {
        return config.getName();
    }

    @Override
    public final int getParameter(String key, int defValue)
    {
        return config.getParameter(key, defValue);
    }

    @Override
    public final String getParameter(String key, String defValue)
    {
        return config.getParameter(key, defValue);
    }

    @Override
    public final String getParameterizedName()
    {
        return config.getParameterizedName();
    }

    @Override
    public final Set<String> getParameterKeys()
    {
        return config.getParameterKeys();
    }

    /**
     * Return parameters found in request URI.
     *
     * @return the parameter map
     */
    @Override
    public final Map<String, String> getParameters()
    {
        return config.getParameters();
    }

    @Override
    public final void setParameter(String key)
    {
        config.setParameter(key);
    }

    @Override
    public final void setParameter(String key, int value)
    {
        config.setParameter(key, value);
    }

    @Override
    public final void setParameter(String key, String value)
    {
        config.setParameter(key, value);
    }

    @Override
    public String toString()
    {
        return config.toString();
    }
}