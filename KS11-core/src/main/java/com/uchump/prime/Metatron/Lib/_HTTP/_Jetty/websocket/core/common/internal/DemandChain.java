package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;

import java.util.function.LongConsumer;

/**
 * This is extended by an {@link Extension} so it can intercept demand calls.
 * Demand is called by the application and the call is forwarded through the {@link ExtensionStack}
 * for every {@link Extension} which implements this interface.
 */
public interface DemandChain
{
    void demand(long n);

    default void setNextDemand(LongConsumer nextDemand)
    {
    }
}