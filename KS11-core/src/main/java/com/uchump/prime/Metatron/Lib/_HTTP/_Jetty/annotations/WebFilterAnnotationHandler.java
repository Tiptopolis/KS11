package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.ClassInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.FieldInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.MethodInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

/**
 * WebFilterAnnotationHandler
 */
public class WebFilterAnnotationHandler extends AbstractDiscoverableAnnotationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(WebFilterAnnotationHandler.class);

    public WebFilterAnnotationHandler(WebAppContext context)
    {
        super(context);
    }

    @Override
    public void handle(ClassInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebFilter".equals(annotationName))
            return;

        WebFilterAnnotation wfAnnotation = new WebFilterAnnotation(_context, info.getClassName(), info.getContainingResource());
        addAnnotation(wfAnnotation);
    }

    @Override
    public void handle(FieldInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebFilter".equals(annotationName))
            return;
        LOG.warn("@WebFilter not applicable for fields: {}.{}", info.getClassInfo().getClassName(), info.getFieldName());
    }

    @Override
    public void handle(MethodInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebFilter".equals(annotationName))
            return;
        LOG.warn("@WebFilter not applicable for methods: {}.{} {}", info.getClassInfo().getClassName(), info.getMethodName(), info.getSignature());
    }
}