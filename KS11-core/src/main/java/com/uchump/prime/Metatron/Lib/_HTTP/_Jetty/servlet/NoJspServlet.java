package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoJspServlet extends HttpServlet
{
    private boolean _warned;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException
    {
        if (!_warned)
            getServletContext().log("No JSP support.  Check that JSP jars are in lib/jsp and that the JSP option has been specified to start.jar");
        _warned = true;

        response.sendError(500, "JSP support not configured");
    }
}