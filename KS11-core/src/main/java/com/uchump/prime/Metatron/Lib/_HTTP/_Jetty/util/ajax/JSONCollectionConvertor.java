package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ajax;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;

public class JSONCollectionConvertor implements JSON.Convertor
{
    @Override
    public void toJSON(Object obj, JSON.Output out)
    {
        out.addClass(obj.getClass());
        Collection<?> collection = (Collection<?>)obj;
        out.add("list", collection.toArray());
    }

    @Override
    public Object fromJSON(Map<String, Object> object)
    {
        try
        {
            Class<?> cls = Loader.loadClass((String)object.get("class"));
            @SuppressWarnings("unchecked")
            Collection<Object> result = (Collection<Object>)cls.getConstructor().newInstance();
            Collections.addAll(result, (Object[])object.get("list"));
            return result;
        }
        catch (Exception x)
        {
            if (x instanceof RuntimeException)
                throw (RuntimeException)x;
            throw new RuntimeException(x);
        }
    }
}