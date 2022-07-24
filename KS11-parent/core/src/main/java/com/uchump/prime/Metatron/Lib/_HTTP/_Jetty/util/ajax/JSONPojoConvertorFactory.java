package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ajax;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Loader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ajax.JSON.Convertor;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ajax.JSON.Output;

public class JSONPojoConvertorFactory implements JSON.Convertor
{
    private static final Logger LOG = LoggerFactory.getLogger(JSONPojoConvertorFactory.class);
    private final JSON _json;
    private final boolean _fromJson;

    public JSONPojoConvertorFactory(JSON json)
    {
        this(json, true);
    }

    /**
     * @param json The JSON instance to use
     * @param fromJSON If true, the class name of the objects is included
     * in the generated JSON and is used to instantiate the object when
     * JSON is parsed (otherwise a Map is used).
     */
    public JSONPojoConvertorFactory(JSON json, boolean fromJSON)
    {
        if (json == null)
            throw new IllegalArgumentException();
        _json = json;
        _fromJson = fromJSON;
    }

    @Override
    public void toJSON(Object obj, Output out)
    {
        Class<?> cls = obj.getClass();
        String clsName = cls.getName();
        Convertor convertor = _json.getConvertorFor(clsName);
        if (convertor == null)
        {
            convertor = new JSONPojoConvertor(cls, _fromJson);
            _json.addConvertorFor(clsName, convertor);
        }
        convertor.toJSON(obj, out);
    }

    @Override
    public Object fromJSON(Map<String, Object> map)
    {
        String clsName = (String)map.get("class");
        if (clsName != null)
        {
            Convertor convertor = _json.getConvertorFor(clsName);
            if (convertor == null)
            {
                try
                {
                    Class<?> cls = Loader.loadClass(clsName);
                    convertor = new JSONPojoConvertor(cls, _fromJson);
                    _json.addConvertorFor(clsName, convertor);
                }
                catch (ClassNotFoundException e)
                {
                    LOG.warn("Unable to find class: {}", clsName, e);
                }
            }
            if (convertor != null)
                return convertor.fromJSON(map);
        }
        return map;
    }
}