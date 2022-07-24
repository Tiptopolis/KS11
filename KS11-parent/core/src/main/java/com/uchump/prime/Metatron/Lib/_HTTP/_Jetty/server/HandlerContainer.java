package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.LifeCycle;

/**
 * A Handler that contains other Handlers.
 * <p>
 * The contained handlers may be one (see @{link {@link org.eclipse.jetty.server.handler.HandlerWrapper})
 * or many (see {@link org.eclipse.jetty.server.handler.HandlerList} or {@link org.eclipse.jetty.server.handler.HandlerCollection}.
 */
@ManagedObject("Handler of Multiple Handlers")
public interface HandlerContainer extends LifeCycle
{

    /**
     * @return array of handlers directly contained by this handler.
     */
    @ManagedAttribute("handlers in this container")
    public Handler[] getHandlers();

    /**
     * @return array of all handlers contained by this handler and it's children
     */
    @ManagedAttribute("all contained handlers")
    public Handler[] getChildHandlers();

    /**
     * @param byclass the child handler class to get
     * @return array of all handlers contained by this handler and it's children of the passed type.
     */
    public Handler[] getChildHandlersByClass(Class<?> byclass);

    /**
     * @param byclass the child handler class to get
     * @param <T> the type of handler
     * @return first handler of all handlers contained by this handler and it's children of the passed type.
     */
    public <T extends Handler> T getChildHandlerByClass(Class<T> byclass);
}