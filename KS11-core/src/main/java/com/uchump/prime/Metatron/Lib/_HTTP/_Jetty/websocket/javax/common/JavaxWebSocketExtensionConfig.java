package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import javax.websocket.Extension;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;

public class JavaxWebSocketExtensionConfig extends ExtensionConfig
{
    public JavaxWebSocketExtensionConfig(Extension ext)
    {
        super(ext.getName());
        for (Extension.Parameter param : ext.getParameters())
        {
            this.setParameter(param.getName(), param.getValue());
        }
    }
}