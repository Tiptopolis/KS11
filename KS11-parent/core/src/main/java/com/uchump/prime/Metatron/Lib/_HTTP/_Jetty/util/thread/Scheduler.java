package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread;

import java.util.concurrent.TimeUnit;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.LifeCycle;

public interface Scheduler extends LifeCycle
{
    interface Task
    {
        boolean cancel();
    }

    Task schedule(Runnable task, long delay, TimeUnit units);
}