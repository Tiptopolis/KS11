package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.jmx;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.ObjectMBean;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.AbstractHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.AbstractHandlerContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandler;

public class AbstractHandlerMBean extends ObjectMBean
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractHandlerMBean.class);

    public AbstractHandlerMBean(Object managedObject)
    {
        super(managedObject);
    }

    @Override
    public String getObjectContextBasis()
    {
        if (_managed != null)
        {
            String basis = null;
            if (_managed instanceof ContextHandler)
            {
                ContextHandler handler = (ContextHandler)_managed;
                String context = getContextName(handler);
                if (context == null)
                    context = handler.getDisplayName();
                if (context != null)
                    return context;
            }
            else if (_managed instanceof AbstractHandler)
            {
                AbstractHandler handler = (AbstractHandler)_managed;
                Server server = handler.getServer();
                if (server != null)
                {
                    ContextHandler context =
                        AbstractHandlerContainer.findContainerOf(server,
                            ContextHandler.class, handler);

                    if (context != null)
                        basis = getContextName(context);
                }
            }
            if (basis != null)
                return basis;
        }
        return super.getObjectContextBasis();
    }

    protected String getContextName(ContextHandler context)
    {
        String name = null;

        if (context.getContextPath() != null && context.getContextPath().length() > 0)
        {
            int idx = context.getContextPath().lastIndexOf('/');
            name = idx < 0 ? context.getContextPath() : context.getContextPath().substring(++idx);
            if (name == null || name.length() == 0)
                name = "ROOT";
        }

        if (name == null && context.getBaseResource() != null)
        {
            try
            {
                if (context.getBaseResource().getFile() != null)
                    name = context.getBaseResource().getFile().getName();
            }
            catch (IOException e)
            {
                LOG.trace("IGNORED", e);
                name = context.getBaseResource().getName();
            }
        }

        if (context.getVirtualHosts() != null && context.getVirtualHosts().length > 0)
            name = '"' + name + "@" + context.getVirtualHosts()[0] + '"';

        return name;
    }
}