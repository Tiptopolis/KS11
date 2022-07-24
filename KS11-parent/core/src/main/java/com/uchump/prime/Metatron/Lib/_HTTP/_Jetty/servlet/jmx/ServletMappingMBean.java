package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.jmx;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.ObjectMBean;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletMapping;

public class ServletMappingMBean extends ObjectMBean
{

    public ServletMappingMBean(Object managedObject)
    {
        super(managedObject);
    }

    @Override
    public String getObjectNameBasis()
    {
        if (_managed != null && _managed instanceof ServletMapping)
        {
            ServletMapping mapping = (ServletMapping)_managed;
            String name = mapping.getServletName();
            if (name != null)
                return name;
        }

        return super.getObjectNameBasis();
    }
}