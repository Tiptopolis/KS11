package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.encoders;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.websocket.Encoder;

/**
 * A {@code Future&lt;Void&gt;} that is already failed as a result of an Encode error
 */
public class EncodeFailedFuture implements Future<Void>
{
    private final String msg;
    private final Throwable cause;

    public EncodeFailedFuture(Object data, Encoder encoder, Class<?> encoderType, Throwable cause)
    {
        this.msg = String.format("Unable to encode %s using %s as %s", data.getClass().getName(), encoder.getClass().getName(), encoderType.getName());
        this.cause = cause;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        return false;
    }

    @Override
    public Void get() throws InterruptedException, ExecutionException
    {
        throw new ExecutionException(msg, cause);
    }

    @Override
    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
    {
        throw new ExecutionException(msg, cause);
    }

    @Override
    public boolean isCancelled()
    {
        return false;
    }

    @Override
    public boolean isDone()
    {
        return true;
    }
}