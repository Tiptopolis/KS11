package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.LeakDetector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.LifeCycle;

public class LeakTrackingConnectionPool extends DuplexConnectionPool
{
    private static final Logger LOG = LoggerFactory.getLogger(LeakTrackingConnectionPool.class);

    private final LeakDetector<Connection> leakDetector = new LeakDetector<>()
    {
        @Override
        protected void leaked(LeakInfo leakInfo)
        {
            LeakTrackingConnectionPool.this.leaked(leakInfo);
        }
    };

    public LeakTrackingConnectionPool(HttpDestination destination, int maxConnections, Callback requester)
    {
        super(destination, maxConnections, requester);
        addBean(leakDetector);
    }

    @Override
    public void close()
    {
        super.close();
        LifeCycle.stop(this);
    }

    @Override
    protected void acquired(Connection connection)
    {
        if (!leakDetector.acquired(connection))
            LOG.info("Connection {}@{} not tracked", connection, leakDetector.id(connection));
    }

    @Override
    protected void released(Connection connection)
    {
        if (!leakDetector.released(connection))
            LOG.info("Connection {}@{} released but not acquired", connection, leakDetector.id(connection));
    }

    protected void leaked(LeakDetector<Connection>.LeakInfo leakInfo)
    {
        LOG.info("Connection {} leaked at:", leakInfo.getResourceDescription(), leakInfo.getStackFrames());
    }
}