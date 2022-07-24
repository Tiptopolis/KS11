package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.URIUtil;

/**
 * Welcome Filter
 * This filter can be used to server an index file for a directory
 * when no index file actually exists (thus the web.xml mechanism does
 * not work).
 *
 * This filter will dispatch requests to a directory (URLs ending with /)
 * to the welcome URL determined by the "welcome" init parameter.  So if
 * the filter "welcome" init parameter is set to "index.do" then a request
 * to "/some/directory/" will be dispatched to "/some/directory/index.do" and
 * will be handled by any servlets mapped to that URL.
 *
 * Requests to "/some/directory" will be redirected to "/some/directory/".
 * @deprecated no replacement is offered, use standard Servlet web.xml welcome features
 */
@Deprecated
public class WelcomeFilter implements Filter
{
    private String welcome;

    @Override
    public void init(FilterConfig filterConfig)
    {
        welcome = filterConfig.getInitParameter("welcome");
        if (welcome == null)
            welcome = "index.html";
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException
    {
        String path = ((HttpServletRequest)request).getServletPath();
        if (welcome != null && path.endsWith("/"))
        {
            String uriInContext = URIUtil.encodePath(URIUtil.addPaths(path, welcome));
            request.getRequestDispatcher(uriInContext).forward(request, response);
        }
        else
            chain.doFilter(request, response);
    }

    @Override
    public void destroy()
    {
    }
}