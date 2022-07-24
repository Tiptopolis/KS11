package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;

/**
 * DiscoveredAnnotation
 *
 * Represents an annotation that has been discovered
 * by scanning source code of WEB-INF/classes and WEB-INF/lib jars.
 */
public abstract class DiscoveredAnnotation
{
    private static final Logger LOG = LoggerFactory.getLogger(DiscoveredAnnotation.class);

    protected WebAppContext _context;
    protected String _className;
    protected Class<?> _clazz;
    protected Resource _resource; //resource it was discovered on, can be null (eg from WEB-INF/classes)

    public abstract void apply();

    public DiscoveredAnnotation(WebAppContext context, String className)
    {
        this(context, className, null);
    }

    public DiscoveredAnnotation(WebAppContext context, String className, Resource resource)
    {
        _context = context;
        _className = className;
        _resource = resource;
    }

    public String getClassName()
    {
        return _className;
    }

    public Resource getResource()
    {
        return _resource;
    }

    public Class<?> getTargetClass()
    {
        if (_clazz != null)
            return _clazz;

        loadClass();

        return _clazz;
    }

    private void loadClass()
    {
        if (_clazz != null)
            return;

        if (_className == null)
            return;

        try
        {
            _clazz = Loader.loadClass(_className);
        }
        catch (Exception e)
        {
            LOG.warn("Unable to load {}", _className, e);
        }
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "[" + getClassName() + "," + getResource() + "]";
    }
}