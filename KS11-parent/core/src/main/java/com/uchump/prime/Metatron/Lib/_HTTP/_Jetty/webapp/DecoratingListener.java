package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;

/**
 * An extended org.eclipse.jetty.servlet.DecoratingListener.
 * The context attribute "org.eclipse.jetty.webapp.DecoratingListener" if
 * not set, is set to the name of the attribute this listener listens for.
 */
public class DecoratingListener extends com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.DecoratingListener
{
    public static final String DECORATOR_ATTRIBUTE = "org.eclipse.jetty.webapp.decorator";

    public DecoratingListener()
    {
        this(DECORATOR_ATTRIBUTE);
    }

    public DecoratingListener(String attributeName)
    {
        this(WebAppContext.getCurrentWebAppContext(), attributeName);
    }

    public DecoratingListener(ServletContextHandler context)
    {
        this(context, DECORATOR_ATTRIBUTE);
    }

    public DecoratingListener(ServletContextHandler context, String attributeName)
    {
        super(context, attributeName);
        checkAndSetAttributeName();
    }

    protected void checkAndSetAttributeName()
    {
        // If not set (by another DecoratingListener), flag the attribute that are
        // listening for.  If more than one DecoratingListener is used then this
        // attribute reflects only the first.
        if (getServletContext().getAttribute(getClass().getName()) != null)
            throw new IllegalStateException("Multiple DecoratingListeners detected");
        getServletContext().setAttribute(getClass().getName(), getAttributeName());
    }
}