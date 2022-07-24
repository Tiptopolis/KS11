package com.uchump.prime.Metatron.Lib._HTTP._Spark;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.routematch.RouteMatch;


public final class RequestResponseFactory {

    private RequestResponseFactory() {
    }

    /**
     * Used to create a request and no RouteMatch is available.
     */
    public static Request create(HttpServletRequest request) {
        return new Request(request);
    }

    public static Request create(RouteMatch match, HttpServletRequest request) {
        return new Request(match, request);
    }

    public static Response create(HttpServletResponse response) {
        return new Response(response);
    }

}