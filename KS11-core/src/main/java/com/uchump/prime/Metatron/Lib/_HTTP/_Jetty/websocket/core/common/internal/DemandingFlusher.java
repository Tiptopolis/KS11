package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;


import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.LongConsumer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.CountingCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IteratingCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.StaticException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.IncomingFrames;


/**
 * <p>This flusher can be used to mutated and fragment {@link Frame}s and forwarded them on towards the application using the
 * {@link IncomingFrames} provided in the constructor. This can split a single incoming frame into n {@link Frame}s which are
 * passed on to the {@link IncomingFrames} one at a time.</p>
 *
 * <p>The asynchronous operation performed by this {@link IteratingCallback} is demanding from upper layer after which
 * {@link #onFrame(Frame, Callback)} will called with the new content.</p>
 *
 * <p>This flusher relies on the interception of demand, and because of this it can only be used in an {@link Extension} which
 * implements the {@link DemandChain} interface. The methods of {@link DemandChain} from the {@link Extension}
 * must be forwarded to this flusher.</p>
 */
public abstract class DemandingFlusher extends IteratingCallback implements DemandChain
{
    private static final Throwable SENTINEL_CLOSE_EXCEPTION = new StaticException("Closed");

    private final IncomingFrames _emitFrame;
    private final AtomicLong _demand = new AtomicLong();
    private final AtomicReference<Throwable> _failure = new AtomicReference<>();
    private LongConsumer _nextDemand;

    private Frame _frame;
    private Callback _callback;
    private boolean _needContent = true;
    private boolean _first = true;

    /**
     * @param emitFrame where frames generated by {@link #handle(Frame, Callback, boolean)} are forwarded.
     */
    public DemandingFlusher(IncomingFrames emitFrame)
    {
        _emitFrame = emitFrame;
    }

    /**
     * <p>Called when there is demand for a single frame to be produced. During this method a single call can be made
     * to {@link #emitFrame(Frame, Callback)} which will forward this frame towards the application. Returning true
     * from this method signals that you are done processing the current Frame, and the next invocation of this
     * method will have the next frame.</p>
     *
     * <p>Note that the callback supplied here is specially wrapped so that you can call
     * it multiple times and it will not be completed more than once. This simplifies the
     * handling of failure cases.</p>
     * @param frame the original frame.
     * @param callback to succeed to release the frame payload.
     * @param first if this is the first time this method has been called for this frame.
     * @return false to continue processing this frame, true to complete processing and get a new frame.
     */
    protected abstract boolean handle(Frame frame, Callback callback, boolean first);

    @Override
    public void demand(long n)
    {
        _demand.getAndUpdate(d -> Math.addExact(d, n));
        iterate();
    }

    @Override
    public void setNextDemand(LongConsumer nextDemand)
    {
        _nextDemand = nextDemand;
    }

    /**
     * Used to supply the flusher with a new frame. This frame should only arrive if demanded
     * through the {@link LongConsumer} provided by {@link #setNextDemand(LongConsumer)}.
     * @param frame the WebSocket frame.
     * @param callback to release frame payload.
     */
    public void onFrame(Frame frame, Callback callback)
    {
        if (_frame != null || _callback != null)
            throw new IllegalStateException("Not expecting onFrame");

        _frame = frame;
        _callback = new CountingCallback(callback, 1);
        succeeded();
    }

    /**
     * Used to close this flusher when there is no explicit failure.
     */
    public void closeFlusher()
    {
        if (_failure.compareAndSet(null, SENTINEL_CLOSE_EXCEPTION))
        {
            failed(SENTINEL_CLOSE_EXCEPTION);
            iterate();
        }
    }

    /**
     * Used to fail this flusher possibly from an external event such as a callback.
     * @param t the failure.
     */
    public void failFlusher(Throwable t)
    {
        if (_failure.compareAndSet(null, t))
        {
            failed(t);
            iterate();
        }
    }

    /**
     * <p>This is used within an implementation of {@link #handle(Frame, Callback, boolean)}
     * to forward a frame onto the next layer of processing.</p>
     *
     * <p>This method should only be called ONCE within each invocation of {@link #handle(Frame, Callback, boolean)}
     * otherwise</p>
     * @param frame the WebSocket frame.
     * @param callback to release frame payload.
     */
    public void emitFrame(Frame frame, Callback callback)
    {
        if (_demand.decrementAndGet() < 0)
            throw new IllegalStateException("Negative Demand");
        _emitFrame.onFrame(frame, callback);
    }

    @Override
    protected Action process() throws Throwable
    {
        while (true)
        {
            Throwable failure = _failure.get();
            if (failure != null)
                throw failure;

            if (_demand.get() <= 0)
                break;

            if (_needContent)
            {
                _needContent = false;
                _nextDemand.accept(1);
                return Action.SCHEDULED;
            }

            boolean first = _first;
            _first = false;
            boolean needContent = handle(_frame, _callback, first);
            if (needContent)
            {
                _needContent = true;
                _first = true;
                _frame = null;
                _callback = null;
            }
        }

        return Action.IDLE;
    }

    @Override
    protected void onCompleteFailure(Throwable cause)
    {
        Throwable suppressed = _failure.getAndSet(cause);
        if (suppressed != null && suppressed != cause)
            cause.addSuppressed(suppressed);

        // This is wrapped with CountingCallback so protects against double succeed/failed.
        if (_callback != null)
            _callback.failed(cause);

        _frame = null;
        _callback = null;
    }
}