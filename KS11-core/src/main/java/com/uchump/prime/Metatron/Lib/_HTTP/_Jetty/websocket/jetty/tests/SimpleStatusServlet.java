package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleStatusServlet extends HttpServlet
{
    private final int statusCode;

    public SimpleStatusServlet(int statusCode)
    {
        this.statusCode = statusCode;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setStatus(this.statusCode);
    }
}