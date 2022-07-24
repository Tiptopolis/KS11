package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Handler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;


/**
 * HandlerList.
 * This extension of {@link HandlerCollection} will call
 * each contained handler in turn until either an exception is thrown, the response
 * is committed or a positive response status is set.
 */
public class HandlerList extends HandlerCollection
{
    public HandlerList()
    {
    }

    public HandlerList(Handler... handlers)
    {
        super(handlers);
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        Handler[] handlers = getHandlers();

        if (handlers != null && isStarted())
        {
            for (int i = 0; i < handlers.length; i++)
            {
                handlers[i].handle(target, baseRequest, request, response);
                if (baseRequest.isHandled())
                    return;
            }
        }
    }
}