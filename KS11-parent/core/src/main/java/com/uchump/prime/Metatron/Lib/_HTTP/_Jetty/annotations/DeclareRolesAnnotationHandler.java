package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import javax.annotation.security.DeclareRoles;
import javax.servlet.Servlet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationIntrospector.AbstractIntrospectableAnnotationHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.ConstraintAware;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.ConstraintSecurityHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

/**
 * DeclaresRolesAnnotationHandler
 */
public class DeclareRolesAnnotationHandler extends AbstractIntrospectableAnnotationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DeclareRolesAnnotationHandler.class);

    public DeclareRolesAnnotationHandler(WebAppContext context)
    {
        super(false, context);
    }

    @Override
    public void doHandle(Class clazz)
    {
        if (!Servlet.class.isAssignableFrom(clazz))
            return; //only applicable on javax.servlet.Servlet derivatives

        if (!(_context.getSecurityHandler() instanceof ConstraintAware))
        {
            LOG.warn("SecurityHandler not ConstraintAware, skipping security annotation processing");
            return;
        }

        DeclareRoles declareRoles = (DeclareRoles)clazz.getAnnotation(DeclareRoles.class);
        if (declareRoles == null)
            return;

        String[] roles = declareRoles.value();

        if (roles != null && roles.length > 0)
        {
            for (String r : roles)
            {
                ((ConstraintSecurityHandler)_context.getSecurityHandler()).addRole(r);
                _context.getMetaData().setOrigin("security-role." + r, declareRoles, clazz);
            }
        }
    }
}