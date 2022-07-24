package com.uchump.prime.Metatron.Lib._HTTP._Spark.route;
/**
 * Holds the servlet routes.
 *
 * @author Per Wendel
 */
public final class ServletRoutes {

    private static Routes routes = null;

    private ServletRoutes() {
    }

    public static synchronized Routes get() {
        if (routes == null) {
            routes = new Routes();
        }
        return routes;
    }

}