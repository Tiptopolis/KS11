package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.annotation.ContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.AbstractLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
/**
 * ServletContainerInitializersStarter
 *
 * Call the onStartup() method on all ServletContainerInitializers, after having
 * found all applicable classes (if any) to pass in as args.
 * @deprecated
 */
@Deprecated
public class ServletContainerInitializersStarter extends AbstractLifeCycle implements ServletContextHandler.ServletContainerInitializerCaller
{
    private static final Logger LOG = LoggerFactory.getLogger(ServletContainerInitializersStarter.class);
    WebAppContext _context;

    public ServletContainerInitializersStarter(WebAppContext context)
    {
        _context = context;
    }

    @Override
    public void doStart()
    {
        List<ContainerInitializer> initializers = (List<ContainerInitializer>)_context.getAttribute(AnnotationConfiguration.CONTAINER_INITIALIZERS);
        if (initializers == null)
            return;

        for (ContainerInitializer i : initializers)
        {
            try
            {
                if (LOG.isDebugEnabled())
                    LOG.debug("Calling ServletContainerInitializer {}", i.getTarget().getClass().getName());
                i.callStartup(_context);
            }
            catch (Exception e)
            {
                LOG.warn("Failed to call startup on {}", i, e);
                throw new RuntimeException(e);
            }
        }
    }
}