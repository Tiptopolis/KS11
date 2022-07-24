package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component;
/**
 * <p>A LifeCycle listener that halts the JVM with exit
 * code {@code 0} when notified of the "started" event.</p>
 */
public class HaltLifeCycleListener implements LifeCycle.Listener
{
    @Override
    public void lifeCycleStarted(LifeCycle lifecycle)
    {
        Runtime.getRuntime().halt(0);
    }
}