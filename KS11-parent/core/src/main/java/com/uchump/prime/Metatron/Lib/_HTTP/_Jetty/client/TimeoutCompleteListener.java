package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Result;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.CyclicTimeout;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.Scheduler;

/**
 * @deprecated Do not use it, use {@link CyclicTimeouts} instead.
 */
@Deprecated
public class TimeoutCompleteListener extends CyclicTimeout implements Response.CompleteListener
{
    private static final Logger LOG = LoggerFactory.getLogger(TimeoutCompleteListener.class);

    private final AtomicReference<Request> requestTimeout = new AtomicReference<>();

    public TimeoutCompleteListener(Scheduler scheduler)
    {
        super(scheduler);
    }

    @Override
    public void onTimeoutExpired()
    {
        Request request = requestTimeout.getAndSet(null);
        if (LOG.isDebugEnabled())
            LOG.debug("Total timeout {} ms elapsed for {} on {}", request.getTimeout(), request, this);
        if (request != null)
            request.abort(new TimeoutException("Total timeout " + request.getTimeout() + " ms elapsed"));
    }

    @Override
    public void onComplete(Result result)
    {
        Request request = requestTimeout.getAndSet(null);
        if (request != null)
        {
            boolean cancelled = cancel();
            if (LOG.isDebugEnabled())
                LOG.debug("Cancelled ({}) timeout for {} on {}", cancelled, request, this);
        }
    }

    void schedule(HttpRequest request, long timeoutAt)
    {
        if (requestTimeout.compareAndSet(null, request))
        {
            long delay = Math.max(0, timeoutAt - System.nanoTime());
            if (LOG.isDebugEnabled())
                LOG.debug("Scheduling timeout in {} ms for {} on {}", TimeUnit.NANOSECONDS.toMillis(delay), request, this);
            schedule(delay, TimeUnit.NANOSECONDS);
        }
    }
}