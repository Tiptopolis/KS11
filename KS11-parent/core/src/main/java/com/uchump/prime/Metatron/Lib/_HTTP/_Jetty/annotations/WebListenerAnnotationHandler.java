package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.ClassInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.FieldInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.MethodInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

public class WebListenerAnnotationHandler extends AbstractDiscoverableAnnotationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(WebListenerAnnotationHandler.class);

    public WebListenerAnnotationHandler(WebAppContext context)
    {
        super(context);
    }

    @Override
    public void handle(ClassInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebListener".equals(annotationName))
            return;

        WebListenerAnnotation wlAnnotation = new WebListenerAnnotation(_context, info.getClassName(), info.getContainingResource());
        addAnnotation(wlAnnotation);
    }

    @Override
    public void handle(FieldInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebListener".equals(annotationName))
            return;
        LOG.warn("@WebListener is not applicable to fields: {}.{}", info.getClassInfo().getClassName(), info.getFieldName());
    }

    @Override
    public void handle(MethodInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebListener".equals(annotationName))
            return;
        LOG.warn("@WebListener is not applicable to methods: {}.{} {}", info.getClassInfo().getClassName(), info.getMethodName(), info.getSignature());
    }
}