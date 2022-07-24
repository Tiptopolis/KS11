package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.preventers;

import java.awt.Toolkit;

/**
 * AWTLeakPreventer
 *
 * See https://issues.jboss.org/browse/AS7-3733
 *
 * The java.awt.Toolkit class has a static field that is the default toolkit.
 * Creating the default toolkit causes the creation of an EventQueue, which has a
 * classloader field initialized by the thread context class loader.
 */
public class AWTLeakPreventer extends AbstractLeakPreventer
{

    @Override
    public void prevent(ClassLoader loader)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Pinning classloader for java.awt.EventQueue using {}", loader);
        Toolkit.getDefaultToolkit();
    }
}