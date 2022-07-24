package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import javax.servlet.Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationIntrospector.AbstractIntrospectableAnnotationHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.Descriptor;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaData;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

public class RunAsAnnotationHandler extends AbstractIntrospectableAnnotationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(RunAsAnnotationHandler.class);

    public RunAsAnnotationHandler(WebAppContext wac)
    {
        //Introspect only the given class for a RunAs annotation, as it is a class level annotation,
        //and according to Common Annotation Spec p2-6 a class-level annotation is not inheritable.
        super(false, wac);
    }

    @Override
    public void doHandle(Class clazz)
    {
        if (!Servlet.class.isAssignableFrom(clazz))
            return;

        javax.annotation.security.RunAs runAs = (javax.annotation.security.RunAs)clazz.getAnnotation(javax.annotation.security.RunAs.class);
        if (runAs != null)
        {
            String role = runAs.value();
            if (role != null)
            {
                for (ServletHolder holder : _context.getServletHandler().getServlets(clazz))
                {
                    MetaData metaData = _context.getMetaData();
                    Descriptor d = metaData.getOriginDescriptor(holder.getName() + ".servlet.run-as");
                    //if a descriptor has already set the value for run-as, do not
                    //let the annotation override it
                    if (d == null)
                    {
                        metaData.setOrigin(holder.getName() + ".servlet.run-as", runAs, clazz);
                        holder.setRunAsRole(role);
                    }
                }
            }
            else
                LOG.warn("Bad value for @RunAs annotation on class {}", clazz.getName());
        }
    }

    public void handleField(String className, String fieldName, int access, String fieldType, String signature, Object value, String annotation)
    {
        LOG.warn("@RunAs annotation not applicable for fields: {}.{}", className, fieldName);
    }

    public void handleMethod(String className, String methodName, int access, String params, String signature, String[] exceptions, String annotation)
    {
        LOG.warn("@RunAs annotation ignored on method: {}.{} {}", className, methodName, signature);
    }
}