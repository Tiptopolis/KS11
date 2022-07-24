package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.io.IOException;
import java.util.function.BiConsumer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility Servlet to make easier testcases
 */
public class BiConsumerServiceServlet extends HttpServlet
{
    private final BiConsumer<HttpServletRequest, HttpServletResponse> consumer;

    public BiConsumerServiceServlet(BiConsumer<HttpServletRequest, HttpServletResponse> consumer)
    {
        this.consumer = consumer;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        this.consumer.accept(req, resp);
    }
}