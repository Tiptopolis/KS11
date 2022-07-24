package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/**
 * A variation of Executor that can confirm if a thread is available immediately
 */
public interface TryExecutor extends Executor
{
    /**
     * Attempt to execute a task.
     *
     * @param task The task to be executed
     * @return True IFF the task has been given directly to a thread to execute.  The task cannot be queued pending the later availability of a Thread.
     */
    boolean tryExecute(Runnable task);

    @Override
    default void execute(Runnable task)
    {
        if (!tryExecute(task))
            throw new RejectedExecutionException();
    }

    public static TryExecutor asTryExecutor(Executor executor)
    {
        if (executor instanceof TryExecutor)
            return (TryExecutor)executor;
        return new NoTryExecutor(executor);
    }

    public static class NoTryExecutor implements TryExecutor
    {
        private final Executor executor;

        public NoTryExecutor(Executor executor)
        {
            this.executor = executor;
        }

        @Override
        public void execute(Runnable task)
        {
            executor.execute(task);
        }

        @Override
        public boolean tryExecute(Runnable task)
        {
            return false;
        }

        @Override
        public String toString()
        {
            return String.format("%s@%x[%s]", getClass().getSimpleName(), hashCode(), executor);
        }
    }

    TryExecutor NO_TRY = new TryExecutor()
    {
        @Override
        public boolean tryExecute(Runnable task)
        {
            return false;
        }

        @Override
        public String toString()
        {
            return "NO_TRY";
        }
    };
}