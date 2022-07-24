package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.jmx;

import java.util.HashMap;
import java.util.Map;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Attributes;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedOperation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.Name;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandler;


@ManagedObject("ContextHandler mbean wrapper")
public class ContextHandlerMBean extends AbstractHandlerMBean
{
    public ContextHandlerMBean(Object managedObject)
    {
        super(managedObject);
    }

    @ManagedAttribute("Map of context attributes")
    public Map<String, Object> getContextAttributes()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Attributes attrs = ((ContextHandler)_managed).getAttributes();
        for (String name : attrs.getAttributeNameSet())
        {
            Object value = attrs.getAttribute(name);
            map.put(name, value);
        }
        return map;
    }

    @ManagedOperation(value = "Set context attribute", impact = "ACTION")
    public void setContextAttribute(@Name(value = "name", description = "attribute name") String name, @Name(value = "value", description = "attribute value") Object value)
    {
        Attributes attrs = ((ContextHandler)_managed).getAttributes();
        attrs.setAttribute(name, value);
    }

    @ManagedOperation(value = "Set context attribute", impact = "ACTION")
    public void setContextAttribute(@Name(value = "name", description = "attribute name") String name, @Name(value = "value", description = "attribute value") String value)
    {
        Attributes attrs = ((ContextHandler)_managed).getAttributes();
        attrs.setAttribute(name, value);
    }

    @ManagedOperation(value = "Remove context attribute", impact = "ACTION")
    public void removeContextAttribute(@Name(value = "name", description = "attribute name") String name)
    {
        Attributes attrs = ((ContextHandler)_managed).getAttributes();
        attrs.removeAttribute(name);
    }
}