package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @param <TYPE> the element type
 */
@SuppressWarnings("serial")
public class HostMap<TYPE> extends HashMap<String, TYPE>
{

    /**
     * Construct empty HostMap.
     */
    public HostMap()
    {
        super(11);
    }

    /**
     * Construct empty HostMap.
     *
     * @param capacity initial capacity
     */
    public HostMap(int capacity)
    {
        super(capacity);
    }

    @Override
    public TYPE put(String host, TYPE object)
        throws IllegalArgumentException
    {
        return super.put(host, object);
    }

    @Override
    public TYPE get(Object key)
    {
        return super.get(key);
    }

    /**
     * Retrieve a lazy list of map entries associated with specified
     * hostname by taking into account the domain suffix matches.
     *
     * @param host hostname
     * @return lazy list of map entries
     */
    public Object getLazyMatches(String host)
    {
        if (host == null)
            return LazyList.getList(super.entrySet());

        int idx = 0;
        String domain = host.trim();
        HashSet<String> domains = new HashSet<String>();
        do
        {
            domains.add(domain);
            if ((idx = domain.indexOf('.')) > 0)
            {
                domain = domain.substring(idx + 1);
            }
        }
        while (idx > 0);

        Object entries = null;
        for (Map.Entry<String, TYPE> entry : super.entrySet())
        {
            if (domains.contains(entry.getKey()))
            {
                entries = LazyList.add(entries, entry);
            }
        }

        return entries;
    }
}