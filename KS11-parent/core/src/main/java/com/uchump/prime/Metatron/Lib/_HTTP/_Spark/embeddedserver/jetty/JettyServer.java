package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.QueuedThreadPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.ThreadPool;

/**
 * Creates Jetty Server instances.
 */
class JettyServer implements JettyServerFactory {

    /**
     * Creates a Jetty server.
     *
     * @param maxThreads          maxThreads
     * @param minThreads          minThreads
     * @param threadTimeoutMillis threadTimeoutMillis
     * @return a new jetty server instance
     */
    public Server create(int maxThreads, int minThreads, int threadTimeoutMillis) {
        Server server;

        if (maxThreads > 0) {
            int max = maxThreads;
            int min = (minThreads > 0) ? minThreads : 8;
            int idleTimeout = (threadTimeoutMillis > 0) ? threadTimeoutMillis : 60000;

            server = new Server(new QueuedThreadPool(max, min, idleTimeout));
        } else {
            server = new Server();
        }

        return server;
    }

    /**
     * Creates a Jetty server with supplied thread pool
     * @param threadPool thread pool
     * @return a new jetty server instance
     */
    @Override
    public Server create(ThreadPool threadPool) {
        return threadPool != null ? new Server(threadPool) : new Server();
    }
}