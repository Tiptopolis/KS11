package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression;

import java.util.zip.Deflater;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Container;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.ThreadPool;

public class DeflaterPool extends CompressionPool<Deflater>
{
    private final int compressionLevel;
    private final boolean nowrap;

    /**
     * Create a Pool of {@link Deflater} instances.
     * <p>
     * If given a capacity equal to zero the Deflaters will not be pooled
     * and will be created on acquire and ended on release.
     * If given a negative capacity equal to zero there will be no size restrictions on the DeflaterPool
     *
     * @param capacity maximum number of Deflaters which can be contained in the pool
     * @param compressionLevel the default compression level for new Deflater objects
     * @param nowrap if true then use GZIP compatible compression for all new Deflater objects
     */
    public DeflaterPool(int capacity, int compressionLevel, boolean nowrap)
    {
        super(capacity);
        this.compressionLevel = compressionLevel;
        this.nowrap = nowrap;
    }

    @Override
    protected Deflater newPooled()
    {
        return new Deflater(compressionLevel, nowrap);
    }

    @Override
    protected void end(Deflater deflater)
    {
        deflater.end();
    }

    @Override
    protected void reset(Deflater deflater)
    {
        deflater.reset();
    }

    public static DeflaterPool ensurePool(Container container)
    {
        DeflaterPool pool = container.getBean(DeflaterPool.class);
        if (pool != null)
            return pool;

        int capacity = CompressionPool.DEFAULT_CAPACITY;
        ThreadPool.SizedThreadPool threadPool = container.getBean(ThreadPool.SizedThreadPool.class);
        if (threadPool != null)
            capacity = threadPool.getMaxThreads();

        pool = new DeflaterPool(capacity, Deflater.DEFAULT_COMPRESSION, true);
        container.addBean(pool, true);
        return pool;
    }
}