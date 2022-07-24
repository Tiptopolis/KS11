package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.jmx;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.ObjectMBean;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Handler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;

/**
 *
 */
@ManagedObject("MBean Wrapper for Server")
public class ServerMBean extends ObjectMBean
{
    private final long startupTime;
    private final Server server;

    public ServerMBean(Object managedObject)
    {
        super(managedObject);
        startupTime = System.currentTimeMillis();
        server = (Server)managedObject;
    }

    @ManagedAttribute("contexts on this server")
    public Handler[] getContexts()
    {
        return server.getChildHandlersByClass(ContextHandler.class);
    }

    @ManagedAttribute("the startup time since January 1st, 1970 (in ms)")
    public long getStartupTime()
    {
        return startupTime;
    }
}