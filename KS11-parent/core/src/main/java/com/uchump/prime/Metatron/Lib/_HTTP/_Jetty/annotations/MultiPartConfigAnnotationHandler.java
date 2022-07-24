package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.annotation.MultipartConfig;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationIntrospector.AbstractIntrospectableAnnotationHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.Descriptor;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaData;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;



/**
 * MultiPartConfigAnnotationHandler
 */
public class MultiPartConfigAnnotationHandler extends AbstractIntrospectableAnnotationHandler
{
    public MultiPartConfigAnnotationHandler(WebAppContext context)
    {
        //TODO verify that MultipartConfig is not inheritable
        super(false, context);
    }

    @Override
    public void doHandle(Class clazz)
    {
        if (!Servlet.class.isAssignableFrom(clazz))
            return;

        MultipartConfig multi = (MultipartConfig)clazz.getAnnotation(MultipartConfig.class);
        if (multi == null)
            return;

        MetaData metaData = _context.getMetaData();

        //TODO: The MultipartConfigElement needs to be set on the ServletHolder's Registration.
        //How to identify the correct Servlet?  If the Servlet has no WebServlet annotation on it, does it mean that this MultipartConfig
        //annotation applies to all declared instances in web.xml/programmatically?
        //Assuming TRUE for now.
        for (ServletHolder holder : _context.getServletHandler().getServlets(clazz))
        {
            Descriptor d = metaData.getOriginDescriptor(holder.getName() + ".servlet.multipart-config");
            //if a descriptor has already set the value for multipart config, do not 
            //let the annotation override it
            if (d == null)
            {
                metaData.setOrigin(holder.getName() + ".servlet.multipart-config", multi, clazz);
                holder.getRegistration().setMultipartConfig(new MultipartConfigElement(multi));
            }
        }
    }
}