package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class PutListenerMap implements Map<String, Object>
{
    private Map<String, Object> map;
    private BiConsumer<String, Object> listener;

    public PutListenerMap(Map<String, Object> map, BiConsumer<String, Object> listener)
    {
        this.map = map;
        this.listener = listener;

        // Notify listener for any existing entries in the Map.
        for (Map.Entry<String, Object> entry : map.entrySet())
        {
            listener.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key)
    {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value)
    {
        listener.accept(key, value);
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key)
    {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m)
    {
        for (Map.Entry<String, Object> entry : map.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear()
    {
        map.clear();
    }

    @Override
    public Set<String> keySet()
    {
        return map.keySet();
    }

    @Override
    public Collection<Object> values()
    {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        return map.entrySet();
    }
}