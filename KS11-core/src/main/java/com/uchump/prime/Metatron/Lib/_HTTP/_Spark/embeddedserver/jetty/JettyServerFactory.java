package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.ThreadPool;

/**
 * This interface can be implemented to provide custom Jetty server instances
 * with specific settings or features.
 */
public interface JettyServerFactory {
    /**
     * Creates a Jetty server.
     *
     * @param maxThreads          maxThreads
     * @param minThreads          minThreads
     * @param threadTimeoutMillis threadTimeoutMillis
     * @return a new jetty server instance
     */
    Server create(int maxThreads, int minThreads, int threadTimeoutMillis);

    Server create(ThreadPool threadPool);
}