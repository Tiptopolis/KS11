package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.strategy;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.AutoLock;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.ExecutionStrategy;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.Invocable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.Invocable.InvocationType;;

/**
 * <p>A strategy where the caller thread iterates over task production, submitting each
 * task to an {@link Executor} for execution.</p>
 */
public class ProduceExecuteConsume implements ExecutionStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(ProduceExecuteConsume.class);

    private final AutoLock _lock = new AutoLock();
    private final Producer _producer;
    private final Executor _executor;
    private State _state = State.IDLE;

    public ProduceExecuteConsume(Producer producer, Executor executor)
    {
        _producer = producer;
        _executor = executor;
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

        // Produce until we no task is found.
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

            // Execute the task.
            if (Invocable.getInvocationType(task) == InvocationType.NON_BLOCKING)
                task.run();
            else
                _executor.execute(task);
        }
    }

    @Override
    public void dispatch()
    {
        _executor.execute(this::produce);
    }

    private enum State
    {
        IDLE, PRODUCE, EXECUTE
    }
}