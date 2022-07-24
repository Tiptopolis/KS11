package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A LifeCycle that when started will stop another LifeCycle
 */
public class StopLifeCycle extends AbstractLifeCycle implements LifeCycle.Listener
{
    private static final Logger LOG = LoggerFactory.getLogger(StopLifeCycle.class);

    private final LifeCycle _lifecycle;

    public StopLifeCycle(LifeCycle lifecycle)
    {
        _lifecycle = lifecycle;
        addEventListener(this);
    }

    @Override
    public void lifeCycleStarted(LifeCycle lifecycle)
    {
        try
        {
            _lifecycle.stop();
        }
        catch (Exception e)
        {
            LOG.warn("Unable to stop", e);
        }
    }
}