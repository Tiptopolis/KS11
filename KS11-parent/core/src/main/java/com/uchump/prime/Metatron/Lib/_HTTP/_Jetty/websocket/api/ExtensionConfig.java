package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Represents an Extension Configuration, as seen during the connection Handshake process.
 */
public interface ExtensionConfig
{
    interface Parser
    {
        ExtensionConfig parse(String parameterizedName);
    }

    private static ExtensionConfig.Parser getParser()
    {
        return ServiceLoader.load(ExtensionConfig.Parser.class).iterator().next();
    }

    static ExtensionConfig parse(String parameterizedName)
    {
        return getParser().parse(parameterizedName);
    }

    String getName();

    int getParameter(String key, int defValue);

    String getParameter(String key, String defValue);

    String getParameterizedName();

    Set<String> getParameterKeys();

    /**
     * Return parameters found in request URI.
     *
     * @return the parameter map
     */
    Map<String, String> getParameters();

    void setParameter(String key);

    void setParameter(String key, int value);

    void setParameter(String key, String value);
}