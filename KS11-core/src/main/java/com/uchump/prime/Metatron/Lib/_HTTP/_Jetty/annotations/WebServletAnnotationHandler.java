package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.ClassInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.FieldInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.MethodInfo;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

/**
 * WebServletAnnotationHandler
 *
 * Process a WebServlet annotation on a class.
 */
public class WebServletAnnotationHandler extends AbstractDiscoverableAnnotationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(WebServletAnnotationHandler.class);

    public WebServletAnnotationHandler(WebAppContext context)
    {
        super(context);
    }

    /**
     * Handle discovering a WebServlet annotation.
     */
    @Override
    public void handle(ClassInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebServlet".equals(annotationName))
            return;

        WebServletAnnotation annotation = new WebServletAnnotation(_context, info.getClassName(), info.getContainingResource());
        addAnnotation(annotation);
    }

    @Override
    public void handle(FieldInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebServlet".equals(annotationName))
            return;

        LOG.warn("@WebServlet annotation not supported for fields");
    }

    @Override
    public void handle(MethodInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebServlet".equals(annotationName))
            return;

        LOG.warn("@WebServlet annotation not supported for methods");
    }
}