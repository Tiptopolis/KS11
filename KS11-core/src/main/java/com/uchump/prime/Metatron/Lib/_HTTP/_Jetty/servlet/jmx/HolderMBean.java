package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.jmx;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.ObjectMBean;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.Holder;

public class HolderMBean extends ObjectMBean
{
    public HolderMBean(Object managedObject)
    {
        super(managedObject);
    }

    @Override
    public String getObjectNameBasis()
    {
        if (_managed != null && _managed instanceof Holder)
        {
            Holder holder = (Holder)_managed;
            String name = holder.getName();
            if (name != null)
                return name;
        }
        return super.getObjectNameBasis();
    }
}