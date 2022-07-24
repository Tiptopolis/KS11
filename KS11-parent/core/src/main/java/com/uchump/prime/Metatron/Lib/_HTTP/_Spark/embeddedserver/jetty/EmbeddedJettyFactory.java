package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.ThreadPool;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.ExceptionMapper;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.EmbeddedServer;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.EmbeddedServerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.http.matching.MatcherFilter;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.route.Routes;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.staticfiles.StaticFilesConfiguration;

/**
 * Creates instances of embedded jetty containers.
 */
public class EmbeddedJettyFactory implements EmbeddedServerFactory {
    private final JettyServerFactory serverFactory;
    private ThreadPool threadPool;
    private boolean httpOnly = true;

    public EmbeddedJettyFactory() {
        this.serverFactory = new JettyServer();
    }

    public EmbeddedJettyFactory(JettyServerFactory serverFactory) {
        this.serverFactory = serverFactory;
    }

    public EmbeddedServer create(Routes routeMatcher,
                                 StaticFilesConfiguration staticFilesConfiguration,
                                 ExceptionMapper exceptionMapper,
                                 boolean hasMultipleHandler) {
        MatcherFilter matcherFilter = new MatcherFilter(routeMatcher, staticFilesConfiguration, exceptionMapper, false, hasMultipleHandler);
        matcherFilter.init(null);

        JettyHandler handler = new JettyHandler(matcherFilter);
        handler.getSessionCookieConfig().setHttpOnly(httpOnly);
        return new EmbeddedJettyServer(serverFactory, handler).withThreadPool(threadPool);
    }

    /**
     * Sets optional thread pool for jetty server.  This is useful for overriding the default thread pool
     * behaviour for example io.dropwizard.metrics.jetty9.InstrumentedQueuedThreadPool.
     *
     * @param threadPool thread pool
     * @return Builder pattern - returns this instance
     */
    public EmbeddedJettyFactory withThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
        return this;
    }

    public EmbeddedJettyFactory withHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }
}