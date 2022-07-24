package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.RequestLog;

/**
 * <p>This handler provides an alternate way (other than {@link Server#setRequestLog(RequestLog)})
 * to log request, that can be applied to a particular handler (eg context).
 * This handler can be used to wrap an individual context for context logging, or can be listed
 * prior to a handler.
 * </p>
 *
 * @see Server#setRequestLog(RequestLog)
 */
public class RequestLogHandler extends HandlerWrapper
{
    private RequestLog _requestLog;

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        if (baseRequest.getDispatcherType() == DispatcherType.REQUEST)
            baseRequest.getHttpChannel().addRequestLog(_requestLog);
        if (_handler != null)
            _handler.handle(target, baseRequest, request, response);
    }

    public void setRequestLog(RequestLog requestLog)
    {
        updateBean(_requestLog, requestLog);
        _requestLog = requestLog;
    }

    public RequestLog getRequestLog()
    {
        return _requestLog;
    }
}