package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.strategy;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.AutoLock;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.ExecutionStrategy;

/**
 * <p>A strategy where the caller thread iterates over task production, submitting each
 * task to an {@link Executor} for execution.</p>
 */
public class ProduceConsume implements ExecutionStrategy, Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(ExecuteProduceConsume.class);

    private final AutoLock _lock = new AutoLock();
    private final Producer _producer;
    private final Executor _executor;
    private State _state = State.IDLE;

    public ProduceConsume(Producer producer, Executor executor)
    {
        this._producer = producer;
        this._executor = executor;
    }

    @Override
    public void produce()
    {
        try (AutoLock lock = _lock.lock())
        {
            switch (_state)
            {
                case IDLE:
                    _state = State.PRODUCE;
                    break;
                case PRODUCE:
                case EXECUTE:
                    _state = State.EXECUTE;
                    return;
                default:
                    throw new IllegalStateException(_state.toString());
            }
        }

        // Iterate until we are complete.
        while (true)
        {
            // Produce a task.
            Runnable task = _producer.produce();
            if (LOG.isDebugEnabled())
                LOG.debug("{} produced {}", _producer, task);

            if (task == null)
            {
                try (AutoLock lock = _lock.lock())
                {
                    switch (_state)
                    {
                        case IDLE:
                            throw new IllegalStateException();
                        case PRODUCE:
                            _state = State.IDLE;
                            return;
                        case EXECUTE:
                            _state = State.PRODUCE;
                            continue;
                        default:
                            throw new IllegalStateException(_state.toString());
                    }
                }
            }

            // Run the task.
            task.run();
        }
    }

    @Override
    public void dispatch()
    {
        _executor.execute(this);
    }

    @Override
    public void run()
    {
        produce();
    }

    private enum State
    {
        IDLE, PRODUCE, EXECUTE
    }
}