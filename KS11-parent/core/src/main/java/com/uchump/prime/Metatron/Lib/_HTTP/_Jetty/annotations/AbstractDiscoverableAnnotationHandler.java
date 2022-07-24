package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.AbstractHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.DiscoveredAnnotation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

/**
 * DiscoverableAnnotationHandler
 *
 * Base class for handling the discovery of an annotation.
 */
public abstract class AbstractDiscoverableAnnotationHandler extends AbstractHandler
{
    protected WebAppContext _context;

    public AbstractDiscoverableAnnotationHandler(WebAppContext context)
    {
        _context = context;
    }

    public void addAnnotation(DiscoveredAnnotation a)
    {
        _context.getMetaData().addDiscoveredAnnotation(a);
    }
}