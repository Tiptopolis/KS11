package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.LeakDetector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;

@ManagedObject
public class LeakTrackingByteBufferPool extends ContainerLifeCycle implements ByteBufferPool
{
    private static final Logger LOG = LoggerFactory.getLogger(LeakTrackingByteBufferPool.class);

    private final LeakDetector<ByteBuffer> leakDetector = new LeakDetector<ByteBuffer>()
    {
        @Override
        public String id(ByteBuffer resource)
        {
            return BufferUtil.toIDString(resource);
        }

        @Override
        protected void leaked(LeakInfo leakInfo)
        {
            leaked.incrementAndGet();
            LeakTrackingByteBufferPool.this.leaked(leakInfo);
        }
    };

    private final AtomicLong leakedAcquires = new AtomicLong(0);
    private final AtomicLong leakedReleases = new AtomicLong(0);
    private final AtomicLong leakedRemoves = new AtomicLong(0);
    private final AtomicLong leaked = new AtomicLong(0);
    private final ByteBufferPool delegate;

    public LeakTrackingByteBufferPool(ByteBufferPool delegate)
    {
        this.delegate = delegate;
        addBean(leakDetector);
        addBean(delegate);
    }

    @Override
    public RetainableByteBufferPool asRetainableByteBufferPool()
    {
        // the retainable pool is just a client of the normal pool, so no special handling required.
        return delegate.asRetainableByteBufferPool();
    }

    @Override
    public ByteBuffer acquire(int size, boolean direct)
    {
        ByteBuffer buffer = delegate.acquire(size, direct);
        boolean acquired = leakDetector.acquired(buffer);
        if (!acquired)
        {
            leakedAcquires.incrementAndGet();
            if (LOG.isDebugEnabled())
                LOG.debug("ByteBuffer leaked acquire for id {}", leakDetector.id(buffer), new Throwable("acquire"));
        }
        return buffer;
    }

    @Override
    public void release(ByteBuffer buffer)
    {
        if (buffer == null)
            return;
        boolean released = leakDetector.released(buffer);
        if (!released)
        {
            leakedReleases.incrementAndGet();
            if (LOG.isDebugEnabled())
                LOG.debug("ByteBuffer leaked release for id {}", leakDetector.id(buffer), new Throwable("release"));
        }
        delegate.release(buffer);
    }

    @Override
    public void remove(ByteBuffer buffer)
    {
        if (buffer == null)
            return;
        boolean released = leakDetector.released(buffer);
        if (!released)
        {
            leakedRemoves.incrementAndGet();
            if (LOG.isDebugEnabled())
                LOG.debug("ByteBuffer leaked remove for id {}", leakDetector.id(buffer), new Throwable("remove"));
        }
        delegate.remove(buffer);
    }

    /**
     * Clears the tracking data returned by {@link #getLeakedAcquires()},
     * {@link #getLeakedReleases()}, {@link #getLeakedResources()}.
     */
    @ManagedAttribute("Clears the tracking data")
    public void clearTracking()
    {
        leakedAcquires.set(0);
        leakedReleases.set(0);
    }

    /**
     * @return count of ByteBufferPool.acquire() calls that detected a leak
     */
    @ManagedAttribute("The number of acquires that produced a leak")
    public long getLeakedAcquires()
    {
        return leakedAcquires.get();
    }

    /**
     * @return count of ByteBufferPool.release() calls that detected a leak
     */
    @ManagedAttribute("The number of releases that produced a leak")
    public long getLeakedReleases()
    {
        return leakedReleases.get();
    }

    /**
     * @return count of ByteBufferPool.remove() calls that detected a leak
     */
    @ManagedAttribute("The number of removes that produced a leak")
    public long getLeakedRemoves()
    {
        return leakedRemoves.get();
    }

    /**
     * @return count of resources that were acquired but not released
     */
    @ManagedAttribute("The number of resources that were leaked")
    public long getLeakedResources()
    {
        return leaked.get();
    }

    protected void leaked(LeakDetector<ByteBuffer>.LeakInfo leakInfo)
    {
        LOG.warn("ByteBuffer {} leaked at: {}", leakInfo.getResourceDescription(), leakInfo.getStackFrames());
    }
}