package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Pool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;

@ManagedObject
public class MultiplexConnectionPool extends AbstractConnectionPool
{
    public MultiplexConnectionPool(HttpDestination destination, int maxConnections, Callback requester, int maxMultiplex)
    {
        this(destination, maxConnections, false, requester, maxMultiplex);
    }

    public MultiplexConnectionPool(HttpDestination destination, int maxConnections, boolean cache, Callback requester, int maxMultiplex)
    {
        this(destination, Pool.StrategyType.FIRST, maxConnections, cache, requester, maxMultiplex);
    }

    public MultiplexConnectionPool(HttpDestination destination, Pool.StrategyType strategy, int maxConnections, boolean cache, Callback requester, int maxMultiplex)
    {
        super(destination, new Pool<>(strategy, maxConnections, cache)
        {
            @Override
            protected int getMaxUsageCount(Connection connection)
            {
                int maxUsage = (connection instanceof MaxUsable)
                    ? ((MaxUsable)connection).getMaxUsageCount()
                    : super.getMaxUsageCount(connection);
                return maxUsage > 0 ? maxUsage : -1;
            }

            @Override
            protected int getMaxMultiplex(Connection connection)
            {
                int multiplex = (connection instanceof Multiplexable)
                    ? ((Multiplexable)connection).getMaxMultiplex()
                    : super.getMaxMultiplex(connection);
                return multiplex > 0 ? multiplex : 1;
            }
        }, requester);
        setMaxMultiplex(maxMultiplex);
    }

    @Deprecated
    public MultiplexConnectionPool(HttpDestination destination, Pool<Connection> pool, Callback requester, int maxMultiplex)
    {
        super(destination, pool, requester);
        setMaxMultiplex(maxMultiplex);
    }

    @Override
    @ManagedAttribute(value = "The multiplexing factor of connections")
    public int getMaxMultiplex()
    {
        return super.getMaxMultiplex();
    }

    @Override
    public void setMaxMultiplex(int maxMultiplex)
    {
        super.setMaxMultiplex(maxMultiplex);
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