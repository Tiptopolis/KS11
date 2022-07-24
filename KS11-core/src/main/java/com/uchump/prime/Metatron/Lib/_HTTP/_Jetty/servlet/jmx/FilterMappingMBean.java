package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.jmx;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.ObjectMBean;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.FilterMapping;

public class FilterMappingMBean extends ObjectMBean
{

    public FilterMappingMBean(Object managedObject)
    {
        super(managedObject);
    }

    @Override
    public String getObjectNameBasis()
    {
        if (_managed != null && _managed instanceof FilterMapping)
        {
            FilterMapping mapping = (FilterMapping)_managed;
            String name = mapping.getFilterName();
            if (name != null)
                return name;
        }

        return super.getObjectNameBasis();
    }
}