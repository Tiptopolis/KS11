package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.AsyncContext;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Attributes;

class AsyncAttributes extends Attributes.Wrapper
{
    private final String _requestURI;
    private final String _contextPath;
    private final ServletPathMapping _mapping;
    private final String _queryString;

    private final String _servletPath;
    private final String _pathInfo;

    public AsyncAttributes(Attributes attributes, String requestUri, String contextPath, String pathInContext, ServletPathMapping mapping, String queryString)
    {
        super(attributes);
        _requestURI = requestUri;
        _contextPath = contextPath;
        _servletPath = mapping == null ? null : mapping.getServletPath();
        _pathInfo = mapping == null ? pathInContext : mapping.getPathInfo();
        _mapping = mapping;
        _queryString = queryString;
    }

    @Override
    public Object getAttribute(String key)
    {
        switch (key)
        {
            case AsyncContext.ASYNC_REQUEST_URI:
                return _requestURI;
            case AsyncContext.ASYNC_CONTEXT_PATH:
                return _contextPath;
            case AsyncContext.ASYNC_SERVLET_PATH:
                return _servletPath;
            case AsyncContext.ASYNC_PATH_INFO:
                return _pathInfo;
            case AsyncContext.ASYNC_QUERY_STRING:
                return _queryString;
            case AsyncContext.ASYNC_MAPPING:
                return _mapping;
            default:
                return super.getAttribute(key);
        }
    }

    @Override
    public Set<String> getAttributeNameSet()
    {
        Set<String> set = new HashSet<>(super.getAttributeNameSet());
        if (_requestURI != null)
            set.add(AsyncContext.ASYNC_REQUEST_URI);
        if (_contextPath != null)
            set.add(AsyncContext.ASYNC_CONTEXT_PATH);
        if (_servletPath != null)
            set.add(AsyncContext.ASYNC_SERVLET_PATH);
        if (_pathInfo != null)
            set.add(AsyncContext.ASYNC_PATH_INFO);
        if (_queryString != null)
            set.add(AsyncContext.ASYNC_QUERY_STRING);
        if (_mapping != null)
            set.add(AsyncContext.ASYNC_MAPPING);
        return set;
    }

    @Override
    public void setAttribute(String key, Object value)
    {
        switch (key)
        {
            case AsyncContext.ASYNC_REQUEST_URI:
            case AsyncContext.ASYNC_CONTEXT_PATH:
            case AsyncContext.ASYNC_SERVLET_PATH:
            case AsyncContext.ASYNC_PATH_INFO:
            case AsyncContext.ASYNC_QUERY_STRING:
            case AsyncContext.ASYNC_MAPPING:
                // Ignore sets for these reserved names as this class is applied
                // we will always override these particular attributes.
                break;
            default:
                super.setAttribute(key, value);
                break;
        }
    }
}