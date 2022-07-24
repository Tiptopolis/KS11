package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.preventers;

import javax.imageio.ImageIO;

/**
 * AppContextLeakPreventer
 *
 * Cause the classloader that is pinned by AppContext.getAppContext() to be
 * a container or system classloader, not a webapp classloader.
 *
 * Inspired by Tomcat JreMemoryLeakPrevention.
 */
public class AppContextLeakPreventer extends AbstractLeakPreventer
{

    @Override
    public void prevent(ClassLoader loader)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Pinning classloader for AppContext.getContext() with{} ", loader);
        ImageIO.getUseCache();
    }
}