package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Attributes;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.AutoLock;

/**
 * An Attributes implementation that holds it's values in an immutable {@link ContainerLifeCycle}
 */
public class AttributeContainerMap extends ContainerLifeCycle implements Attributes
{
    private final AutoLock _lock = new AutoLock();
    private final Map<String, Object> _map = new HashMap<>();

    @Override
    public void setAttribute(String name, Object attribute)
    {
        try (AutoLock l = _lock.lock())
        {
            Object old = _map.put(name, attribute);
            updateBean(old, attribute);
        }
    }

    @Override
    public void removeAttribute(String name)
    {
        try (AutoLock l = _lock.lock())
        {
            Object removed = _map.remove(name);
            if (removed != null)
                removeBean(removed);
        }
    }

    @Override
    public Object getAttribute(String name)
    {
        try (AutoLock l = _lock.lock())
        {
            return _map.get(name);
        }
    }

    @Override
    public Enumeration<String> getAttributeNames()
    {
        try (AutoLock l = _lock.lock())
        {
            return Collections.enumeration(_map.keySet());
        }
    }

    @Override
    public Set<String> getAttributeNameSet()
    {
        try (AutoLock l = _lock.lock())
        {
            return _map.keySet();
        }
    }

    @Override
    public void clearAttributes()
    {
        try (AutoLock l = _lock.lock())
        {
            _map.clear();
            this.removeBeans();
        }
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException
    {
        Dumpable.dumpObject(out, this);
        Dumpable.dumpMapEntries(out, indent, _map, true);
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x{size=%d}", this.getClass().getSimpleName(), hashCode(), _map.size());
    }
}