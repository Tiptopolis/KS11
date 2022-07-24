package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression;

import java.io.Closeable;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Pool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;

@ManagedObject
public abstract class CompressionPool<T> extends ContainerLifeCycle
{
    public static final int DEFAULT_CAPACITY = 1024;

    private int _capacity;
    private Pool<Entry> _pool;

    /**
     * Create a Pool of {@link T} instances.
     *
     * If given a capacity equal to zero the Objects will not be pooled
     * and will be created on acquire and ended on release.
     * If given a negative capacity equal to zero there will be no size restrictions on the Pool
     *
     * @param capacity maximum number of Objects which can be contained in the pool
     */
    public CompressionPool(int capacity)
    {
        _capacity = capacity;
    }

    public int getCapacity()
    {
        return _capacity;
    }

    public void setCapacity(int capacity)
    {
        if (isStarted())
            throw new IllegalStateException("Already Started");
        _capacity = capacity;
    }

    public Pool<Entry> getPool()
    {
        return _pool;
    }

    protected abstract T newPooled();

    protected abstract void end(T object);

    protected abstract void reset(T object);

    /**
     * @return Object taken from the pool if it is not empty or a newly created Object
     */
    public Entry acquire()
    {
        Entry entry = null;
        if (_pool != null)
        {
            Pool<Entry>.Entry acquiredEntry = _pool.acquire(e -> new Entry(newPooled(), e));
            if (acquiredEntry != null)
                entry = acquiredEntry.getPooled();
        }

        return (entry == null) ? new Entry(newPooled()) : entry;
    }

    /**
     * @param entry returns this Object to the pool or calls {@link #end(Object)} if the pool is full.
     */
    public void release(Entry entry)
    {
        entry.release();
    }

    @Override
    protected void doStart() throws Exception
    {
        if (_capacity > 0)
        {
            _pool = new Pool<>(Pool.StrategyType.RANDOM, _capacity, true);
            addBean(_pool);
        }
        super.doStart();
    }

    @Override
    public void doStop() throws Exception
    {
        if (_pool != null)
        {
            _pool.close();
            removeBean(_pool);
            _pool = null;
        }
        super.doStop();
    }

    public class Entry implements Closeable
    {
        private final T _value;
        private final Pool<Entry>.Entry _entry;

        Entry(T value)
        {
            this(value, null);
        }

        Entry(T value, Pool<Entry>.Entry entry)
        {
            _value = value;
            _entry = entry;
        }

        public T get()
        {
            return _value;
        }

        public void release()
        {
            // Reset the value for the next usage.
            reset(_value);

            if (_entry != null)
            {
                // If release return false, the entry should be removed and the object should be disposed.
                if (!_pool.release(_entry))
                {
                    if (_pool.remove(_entry))
                        close();
                }
            }
            else
            {
                close();
            }
        }

        @Override
        public void close()
        {
            end(_value);
        }
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x{%s,size=%d,capacity=%s}",
            getClass().getSimpleName(),
            hashCode(),
            getState(),
            (_pool == null) ? -1 : _pool.size(),
            _capacity);
    }
}