package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Pool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;

/**
 * <p>A {@link ConnectionPool} that provides connections
 * randomly among the ones that are available.</p>
 */
@ManagedObject
public class RandomConnectionPool extends MultiplexConnectionPool
{
    public RandomConnectionPool(HttpDestination destination, int maxConnections, Callback requester, int maxMultiplex)
    {
        super(destination, Pool.StrategyType.RANDOM, maxConnections, false, requester, maxMultiplex);
    }
}