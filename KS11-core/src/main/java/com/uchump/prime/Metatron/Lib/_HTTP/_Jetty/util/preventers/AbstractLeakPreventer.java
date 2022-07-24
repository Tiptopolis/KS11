package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.preventers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.AbstractLifeCycle;

/**
 * AbstractLeakPreventer
 *
 * Abstract base class for code that seeks to avoid pinning of webapp classloaders by using the jetty classloader to
 * proactively call the code that pins them (generally pinned as static data members, or as static
 * data members that are daemon threads (which use the context classloader)).
 *
 * Instances of subclasses of this class should be set with Server.addBean(), which will
 * ensure that they are called when the Server instance starts up, which will have the jetty
 * classloader in scope.
 */
public abstract class AbstractLeakPreventer extends AbstractLifeCycle
{
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractLeakPreventer.class);

    public abstract void prevent(ClassLoader loader);

    @Override
    protected void doStart() throws Exception
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            prevent(getClass().getClassLoader());
            super.doStart();
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(loader);
        }
    }
}