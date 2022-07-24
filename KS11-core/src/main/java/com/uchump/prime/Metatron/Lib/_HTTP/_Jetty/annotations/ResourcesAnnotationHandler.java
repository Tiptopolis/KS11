package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationIntrospector.AbstractIntrospectableAnnotationHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;

public class ResourcesAnnotationHandler extends AbstractIntrospectableAnnotationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(ResourcesAnnotationHandler.class);

    public ResourcesAnnotationHandler(WebAppContext wac)
    {
        super(true, wac);
    }

    @Override
    public void doHandle(Class<?> clazz)
    {
        Resources resources = (Resources)clazz.getAnnotation(Resources.class);
        if (resources != null)
        {
            Resource[] resArray = resources.value();
            if (resArray == null || resArray.length == 0)
            {
                LOG.warn("Skipping empty or incorrect Resources annotation on {}", clazz.getName());
                return;
            }

            for (int j = 0; j < resArray.length; j++)
            {
                String name = resArray[j].name();
                String mappedName = resArray[j].mappedName();

                if (name == null || name.trim().equals(""))
                    throw new IllegalStateException("Class level Resource annotations must contain a name (Common Annotations Spec Section 2.3)");

                try
                {
                    //TODO don't ignore the shareable, auth etc etc

                    if (!com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi.NamingEntryUtil.bindToENC(_context, name, mappedName))
                        if (!com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi.NamingEntryUtil.bindToENC(_context.getServer(), name, mappedName))
                            LOG.warn("Skipping Resources(Resource) annotation on {} for name {}: no resource bound at {}",
                                    clazz.getName(), name, (mappedName == null ? name : mappedName));
                }
                catch (NamingException e)
                {
                    LOG.warn("Unable to bind {} to {}", name, mappedName, e);
                }
            }
        }
    }
}