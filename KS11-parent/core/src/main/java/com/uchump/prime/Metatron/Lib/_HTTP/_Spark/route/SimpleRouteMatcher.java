package com.uchump.prime.Metatron.Lib._HTTP._Spark.route;

import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.routematch.RouteMatch;

/**
 * Kept just for not breaking API.
 *
 * @deprecated see {@link spark.route.Routes}
 */
public class SimpleRouteMatcher extends Routes {

    /**
     * @param route      the route
     * @param acceptType the accept type
     * @param target     the target
     * @deprecated
     */
    public void parseValidateAddRoute(String route, String acceptType, Object target) {
        add(route, acceptType, target);
    }

    /**
     * @param httpMethod the HttpMethod
     * @param path       the path
     * @param acceptType the accept type
     * @return the RouteMatch object
     * @deprecated
     */
    public RouteMatch findTargetForRequestedRoute(HttpMethod httpMethod, String path, String acceptType) {
        return find(httpMethod, path, acceptType);
    }

    /**
     * @param httpMethod the HttpMethod
     * @param path       the path
     * @param acceptType the accept type
     * @return list of RouteMatch objects
     * @deprecated
     */
    public List<RouteMatch> findTargetsForRequestedRoute(HttpMethod httpMethod, String path, String acceptType) {
        return findMultiple(httpMethod, path, acceptType);
    }

    /**
     * @deprecated
     */
    public void clearRoutes() {
        clear();
    }

    /**
     * @param path       the path
     * @param httpMethod the http method name
     * @return true if route removed, false otherwise
     * @deprecated
     */
    public boolean removeRoute(String path, String httpMethod) {
        return remove(path, httpMethod);
    }

    /**
     * @param path   the path
     * @return true if route removed, false otherwise
     * @deprecated
     */
    public boolean removeRoute(String path) {
        return remove(path);
    }

}