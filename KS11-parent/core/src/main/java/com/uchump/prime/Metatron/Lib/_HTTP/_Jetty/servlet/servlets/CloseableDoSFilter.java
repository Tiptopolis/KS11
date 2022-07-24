package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;

/**
 * This is an extension to {@link DoSFilter} that uses Jetty APIs to
 * abruptly close the connection when the request times out.
 */

public class CloseableDoSFilter extends DoSFilter
{
    @Override
    protected void onRequestTimeout(HttpServletRequest request, HttpServletResponse response, Thread handlingThread)
    {
        Request baseRequest = Request.getBaseRequest(request);
        baseRequest.getHttpChannel().getEndPoint().close();
    }
}