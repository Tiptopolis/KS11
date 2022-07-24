package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import java.util.Objects;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.DecoratedObjectFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Decorator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

/**
 * AnnotationDecorator
 */
public class AnnotationDecorator implements Decorator
{
    protected AnnotationIntrospector _introspector;
    protected WebAppContext _context;

    public AnnotationDecorator(WebAppContext context)
    {
        _context = Objects.requireNonNull(context);
        _introspector = new AnnotationIntrospector(_context);
        registerHandlers();
    }

    private void registerHandlers()
    {
        _introspector.registerHandler(new ResourceAnnotationHandler(_context));
        _introspector.registerHandler(new ResourcesAnnotationHandler(_context));
        _introspector.registerHandler(new RunAsAnnotationHandler(_context));
        _introspector.registerHandler(new PostConstructAnnotationHandler(_context));
        _introspector.registerHandler(new PreDestroyAnnotationHandler(_context));
        _introspector.registerHandler(new DeclareRolesAnnotationHandler(_context));
        _introspector.registerHandler(new MultiPartConfigAnnotationHandler(_context));
        _introspector.registerHandler(new ServletSecurityAnnotationHandler(_context));
    }

    /**
     * Look for annotations that can be discovered with introspection:
     * <ul>
     * <li> Resource </li>
     * <li> Resources </li>
     * <li> RunAs </li>
     * <li> PostConstruct </li>
     * <li> PreDestroy </li>
     * <li> DeclareRoles </li>
     * <li> MultiPart </li>
     * <li> ServletSecurity</li>
     * </ul>
     *
     * @param o the object to introspect
     * @param metaInfo information about the object to introspect
     */
    protected void introspect(Object o, Object metaInfo)
    {
        if (o == null)
            return;
        _introspector.introspect(o, metaInfo);
    }

    @Override
    public Object decorate(Object o)
    {
        introspect(o, DecoratedObjectFactory.getAssociatedInfo());
        return o;
    }

    @Override
    public void destroy(Object o)
    {

    }
}