package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpContentResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.ContentResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Result;

/**
 * A {@link BufferingResponseListener} that is also a {@link Future}, to allow applications
 * to block (indefinitely or for a timeout) until {@link #onComplete(Result)} is called,
 * or to {@link #cancel(boolean) abort} the request/response conversation.
 * <p>
 * Typical usage is:
 * <pre>
 * Request request = httpClient.newRequest(...)...;
 * FutureResponseListener listener = new FutureResponseListener(request);
 * request.send(listener); // Asynchronous send
 * ContentResponse response = listener.get(5, TimeUnit.SECONDS); // Timed block
 * </pre>
 */
public class FutureResponseListener extends BufferingResponseListener implements Future<ContentResponse>
{
    private final CountDownLatch latch = new CountDownLatch(1);
    private final Request request;
    private ContentResponse response;
    private Throwable failure;
    private volatile boolean cancelled;

    public FutureResponseListener(Request request)
    {
        this(request, 2 * 1024 * 1024);
    }

    public FutureResponseListener(Request request, int maxLength)
    {
        super(maxLength);
        this.request = request;
    }

    public Request getRequest()
    {
        return request;
    }

    @Override
    public void onComplete(Result result)
    {
        response = new HttpContentResponse(result.getResponse(), getContent(), getMediaType(), getEncoding());
        failure = result.getFailure();
        latch.countDown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        cancelled = true;
        return request.abort(new CancellationException());
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public boolean isDone()
    {
        return latch.getCount() == 0 || isCancelled();
    }

    @Override
    public ContentResponse get() throws InterruptedException, ExecutionException
    {
        latch.await();
        return getResult();
    }

    @Override
    public ContentResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
    {
        boolean expired = !latch.await(timeout, unit);
        if (expired)
            throw new TimeoutException();
        return getResult();
    }

    private ContentResponse getResult() throws ExecutionException
    {
        if (isCancelled())
            throw (CancellationException)new CancellationException().initCause(failure);
        if (failure != null)
            throw new ExecutionException(failure);
        return response;
    }
}