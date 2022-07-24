package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;

public class ExtensionConfigParser implements com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.ExtensionConfig.Parser
{
    /**
     * Parse a single parameterized name.
     *
     * @param parameterizedName the parameterized name
     * @return the ExtensionConfig
     */
    @Override
    public JettyExtensionConfig parse(String parameterizedName)
    {
        return new JettyExtensionConfig(ExtensionConfig.parse(parameterizedName));
    }
}