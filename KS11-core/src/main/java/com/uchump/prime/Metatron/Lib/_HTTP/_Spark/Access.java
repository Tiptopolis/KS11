package com.uchump.prime.Metatron.Lib._HTTP._Spark;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.routematch.RouteMatch;

/**
 * Provides access to package protected methods. JUST FOR INTERNAL USE. NOT PART OF PUBLIC SPARK API.
 */
public final class Access {

    private Access() {
        // hidden
    }

    public static void changeMatch(Request request, RouteMatch match) {
        request.changeMatch(match);
    }

}