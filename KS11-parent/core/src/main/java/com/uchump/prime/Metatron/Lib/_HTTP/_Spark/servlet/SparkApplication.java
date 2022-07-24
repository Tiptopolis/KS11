package com.uchump.prime.Metatron.Lib._HTTP._Spark.servlet;

/**
 * The application entry point when Spark is run in a servlet context.
 *
 * @author Per Wendel
 */
public interface SparkApplication {

    /**
     * Invoked from the SparkFilter. Add routes here.
     */
    void init();

    /**
     * Invoked from the SparkFilter.
     */
    default void destroy() {}
}