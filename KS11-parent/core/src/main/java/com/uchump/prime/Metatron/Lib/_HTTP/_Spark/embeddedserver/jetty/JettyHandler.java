package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session.SessionHandler;


/**
 * Simple Jetty Handler
 *
 * @author Per Wendel
 */
public class JettyHandler extends SessionHandler {

    private Filter filter;

    public JettyHandler(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void doHandle(
            String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        HttpRequestWrapper wrapper = new HttpRequestWrapper(request);
        filter.doFilter(wrapper, response, null);

        if (wrapper.notConsumed()) {
            baseRequest.setHandled(false);
        } else {
            baseRequest.setHandled(true);
        }

    }

}