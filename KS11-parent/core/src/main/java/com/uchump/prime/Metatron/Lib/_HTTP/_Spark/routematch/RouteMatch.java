package com.uchump.prime.Metatron.Lib._HTTP._Spark.routematch;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.route.HttpMethod;

/**
 * @author Per Wendel
 */
public class RouteMatch {

    private Object target;
    private String matchUri;
    private String requestURI;
    private String acceptType;
    private HttpMethod httpMethod;

    public RouteMatch(Object target, String matchUri, String requestUri, String acceptType) {
        this(target, matchUri, requestUri, acceptType, null);
     }

    public RouteMatch(Object target, String matchUri, String requestUri, String acceptType, HttpMethod httpMethod) {
        super();
        this.target = target;
        this.matchUri = matchUri;
        this.requestURI = requestUri;
        this.acceptType = acceptType;
        this.httpMethod = httpMethod;
    }

    /**
     * @return the accept type
     */
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * @return the accept type
     */
    public String getAcceptType() {
        return acceptType;
    }

    /**
     * @return the target
     */
    public Object getTarget() {
        return target;
    }


    /**
     * @return the matchUri
     */
    public String getMatchUri() {
        return matchUri;
    }


    /**
     * @return the requestUri
     */
    public String getRequestURI() {
        return requestURI;
    }


}