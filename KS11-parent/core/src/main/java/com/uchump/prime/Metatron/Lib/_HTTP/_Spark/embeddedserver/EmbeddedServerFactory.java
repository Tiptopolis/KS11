package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.ExceptionMapper;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.route.Routes;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.staticfiles.StaticFilesConfiguration;

/**
 * @author Per Wendel
 */
public interface EmbeddedServerFactory {

    @Deprecated
    default EmbeddedServer create(Routes routeMatcher, StaticFilesConfiguration staticFilesConfiguration, boolean hasMultipleHandler) {
        return create(routeMatcher, staticFilesConfiguration, ExceptionMapper.getServletInstance(), hasMultipleHandler);
    }

    /**
     * Creates an embedded server instance.
     *
     * @param routeMatcher The route matcher
     * @param staticFilesConfiguration The static files configuration object
     * @param hasMultipleHandler true if other handlers exist
     * @return the created instance
     */
    public EmbeddedServer create(Routes routeMatcher, StaticFilesConfiguration staticFilesConfiguration, ExceptionMapper exceptionMapper, boolean hasMultipleHandler);
}