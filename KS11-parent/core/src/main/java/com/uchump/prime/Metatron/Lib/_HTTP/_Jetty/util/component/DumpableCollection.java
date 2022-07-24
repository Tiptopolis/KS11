package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DumpableCollection implements Dumpable
{
    private final String _name;
    private final Collection<?> _collection;

    public DumpableCollection(String name, Collection<?> collection)
    {
        _name = name;
        _collection = collection;
    }

    public static DumpableCollection fromArray(String name, Object[] array)
    {
        return new DumpableCollection(name, array == null ? Collections.emptyList() : Arrays.asList(array));
    }

    public static DumpableCollection from(String name, Object... items)
    {
        return new DumpableCollection(name, items == null ? Collections.emptyList() : Arrays.asList(items));
    }

    public static DumpableCollection from(String name, Collection<?> collection)
    {
        return new DumpableCollection(name, collection);
    }

    @Override
    public String dump()
    {
        return Dumpable.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException
    {
        Object[] array = (_collection == null ? null : _collection.toArray());
        Dumpable.dumpObjects(out, indent, _name + " size=" + (array == null ? 0 : array.length), array);
    }
}