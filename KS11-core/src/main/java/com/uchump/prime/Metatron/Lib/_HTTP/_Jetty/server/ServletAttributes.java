package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.util.Set;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Attributes;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.AttributesMap;

/**
 * An implementation of Attributes that supports the standard async attributes.
 *
 * This implementation delegates to an internal {@link AttributesMap} instance, which
 * can optionally be wrapped with a {@link AsyncAttributes} instance. This allows async
 * attributes to be applied underneath any other attribute wrappers.
 */
public class ServletAttributes implements Attributes
{
    private final Attributes _attributes = new AttributesMap();
    private AsyncAttributes _asyncAttributes;

    public void setAsyncAttributes(String requestURI, String contextPath, String pathInContext, ServletPathMapping servletPathMapping, String queryString)
    {
        _asyncAttributes = new AsyncAttributes(_attributes, requestURI, contextPath, pathInContext, servletPathMapping, queryString);
    }

    private Attributes getAttributes()
    {
        return (_asyncAttributes == null) ? _attributes : _asyncAttributes;
    }

    @Override
    public void removeAttribute(String name)
    {
        getAttributes().removeAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object attribute)
    {
        getAttributes().setAttribute(name, attribute);
    }

    @Override
    public Object getAttribute(String name)
    {
        return getAttributes().getAttribute(name);
    }

    @Override
    public Set<String> getAttributeNameSet()
    {
        return getAttributes().getAttributeNameSet();
    }

    @Override
    public void clearAttributes()
    {
        getAttributes().clearAttributes();
        _asyncAttributes = null;
    }
}