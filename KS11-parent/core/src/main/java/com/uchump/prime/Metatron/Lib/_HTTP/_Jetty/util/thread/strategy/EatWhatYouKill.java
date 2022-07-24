package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.strategy;
import java.util.concurrent.Executor;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.ExecutionStrategy.Producer;

/**
 * @deprecated This class has been renamed to {@link AdaptiveExecutionStrategy}
 */
@Deprecated(forRemoval = true)
public class EatWhatYouKill extends AdaptiveExecutionStrategy
{
    public EatWhatYouKill(Producer producer, Executor executor)
    {
        super(producer, executor);
    }
}