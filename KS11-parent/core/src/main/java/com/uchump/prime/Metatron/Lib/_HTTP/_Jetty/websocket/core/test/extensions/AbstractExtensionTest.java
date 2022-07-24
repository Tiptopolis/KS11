package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.extensions;
import org.junit.jupiter.api.BeforeEach;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;

public abstract class AbstractExtensionTest
{
    public WebSocketComponents components = new WebSocketComponents();

    protected ExtensionTool clientExtensions;
    protected ExtensionTool serverExtensions;

    @BeforeEach
    public void init()
    {
        clientExtensions = new ExtensionTool(components.getBufferPool());
        serverExtensions = new ExtensionTool(components.getBufferPool());
    }
}