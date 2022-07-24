package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An empty trie implementation that never contains anything and never accepts new entries.
 *
 * @param <V> the entry type
 */
class EmptyTrie<V> extends AbstractTrie<V>
{
    @SuppressWarnings("rawtypes")
    private static final EmptyTrie SENSITIVE = new EmptyTrie<>(true);
    @SuppressWarnings("rawtypes")
    private static final EmptyTrie INSENSITIVE = new EmptyTrie<>(false);

    private EmptyTrie(boolean caseSensitive)
    {
        super(caseSensitive);
    }

    @SuppressWarnings("unchecked")
    public static <V> EmptyTrie<V> instance(boolean caseSensitive)
    {
        return caseSensitive ? SENSITIVE : INSENSITIVE;
    }

    @Override
    public void clear()
    {
    }

    @Override
    public V get(String s)
    {
        return null;
    }

    @Override
    public V get(ByteBuffer b)
    {
        return null;
    }

    @Override
    public V get(String s, int offset, int len)
    {
        return null;
    }

    @Override
    public V get(ByteBuffer b, int offset, int len)
    {
        return null;
    }

    @Override
    public V getBest(String s)
    {
        return null;
    }

    @Override
    public V getBest(byte[] b, int offset, int len)
    {
        return null;
    }

    @Override
    public V getBest(ByteBuffer b)
    {
        return null;
    }

    @Override
    public V getBest(byte[] b)
    {
        return null;
    }

    @Override
    public V getBest(String s, int offset, int len)
    {
        return null;
    }

    @Override
    public V getBest(ByteBuffer b, int offset, int len)
    {
        return null;
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public Set<String> keySet()
    {
        return Collections.emptySet();
    }

    @Override
    public boolean put(V v)
    {
        Objects.requireNonNull(v);
        return false;
    }

    @Override
    public boolean put(String s, V v)
    {
        Objects.requireNonNull(s);
        return false;
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    protected boolean putAll(Map<String, V> contents)
    {
        return false;
    }
}