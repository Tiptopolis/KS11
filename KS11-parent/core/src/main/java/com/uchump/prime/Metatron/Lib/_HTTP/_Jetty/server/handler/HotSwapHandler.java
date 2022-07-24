package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Handler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;

/**
 * A <code>HandlerContainer</code> that allows a hot swap of a wrapped handler.
 */
public class HotSwapHandler extends AbstractHandlerContainer
{
    private volatile Handler _handler;

    /**
     *
     */
    public HotSwapHandler()
    {
    }

    /**
     * @return Returns the handlers.
     */
    public Handler getHandler()
    {
        return _handler;
    }

    /**
     * @return Returns the handlers.
     */
    @Override
    public Handler[] getHandlers()
    {
        Handler handler = _handler;
        if (handler == null)
            return new Handler[0];
        return new Handler[]{handler};
    }

    /**
     * @param handler Set the {@link Handler} which should be wrapped.
     */
    public void setHandler(Handler handler)
    {
        try
        {
            Server server = getServer();
            if (handler != null)
                handler.setServer(server);
            updateBean(_handler, handler, true);
            _handler = handler;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doStart() throws Exception
    {
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception
    {
        super.doStop();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Handler handler = _handler;
        if (handler != null && isStarted() && handler.isStarted())
        {
            handler.handle(target, baseRequest, request, response);
        }
    }

    @Override
    protected void expandChildren(List<Handler> list, Class<?> byClass)
    {
        Handler handler = _handler;
        if (handler != null)
            expandHandler(handler, list, byClass);
    }

    @Override
    public void destroy()
    {
        if (!isStopped())
            throw new IllegalStateException("!STOPPED");
        Handler child = getHandler();
        if (child != null)
        {
            setHandler(null);
            child.destroy();
        }
        super.destroy();
    }
}