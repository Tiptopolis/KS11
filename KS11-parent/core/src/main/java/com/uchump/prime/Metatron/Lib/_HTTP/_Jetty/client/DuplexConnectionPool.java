package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Pool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;

@ManagedObject
public class DuplexConnectionPool extends AbstractConnectionPool
{
    public DuplexConnectionPool(HttpDestination destination, int maxConnections, Callback requester)
    {
        this(destination, maxConnections, false, requester);
    }

    public DuplexConnectionPool(HttpDestination destination, int maxConnections, boolean cache, Callback requester)
    {
        super(destination, Pool.StrategyType.FIRST, maxConnections, cache, requester);
    }

    @Deprecated
    public DuplexConnectionPool(HttpDestination destination, Pool<Connection> pool, Callback requester)
    {
        super(destination, pool, requester);
    }

    @Override
    @ManagedAttribute(value = "The maximum amount of times a connection is used before it gets closed")
    public int getMaxUsageCount()
    {
        return super.getMaxUsageCount();
    }

    @Override
    public void setMaxUsageCount(int maxUsageCount)
    {
        super.setMaxUsageCount(maxUsageCount);
    }
}