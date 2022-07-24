package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.util.concurrent.TimeUnit;

public final class Timeouts
{
    public static final long CONNECT_MS = TimeUnit.SECONDS.toMillis(10);
    public static final long OPEN_EVENT_MS = TimeUnit.SECONDS.toMillis(10);
    public static final long CLOSE_EVENT_MS = TimeUnit.SECONDS.toMillis(10);
}