package com.uchump.prime.Metatron.Lib._HTTP._Spark;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.utils.decoding.Wrapper;

/**
 * FilterImpl is created from a path, acceptType and Filter. This is encapsulate the information needed in the route
 * matcher in a single container.
 *
 * @author Per Wendel
 */
public abstract class FilterImpl implements Filter, Wrapper {

    static final String DEFAULT_ACCEPT_TYPE = "*/*";

    private String path;
    private String acceptType;
    private Filter delegate;

    /**
     * Prefix the path (used for {@link Service#path})
     *
     * @param prefix the prefix
     * @return itself for easy chaining
     */
    public FilterImpl withPrefix(String prefix) {
        this.path = prefix + this.path;
        return this;
    }

    /**
     * Wraps the filter in FilterImpl
     *
     * @param path   the path
     * @param filter the filter
     * @return the wrapped route
     */
    static FilterImpl create(final String path, final Filter filter) {
        return create(path, DEFAULT_ACCEPT_TYPE, filter);
    }

    /**
     * Wraps the filter in FilterImpl
     *
     * @param path       the path
     * @param acceptType the accept type
     * @param filter     the filter
     * @return the wrapped route
     */
    static FilterImpl create(final String path, String acceptType, final Filter filter) {
        if (acceptType == null) {
            acceptType = DEFAULT_ACCEPT_TYPE;
        }
        return new FilterImpl(path, acceptType, filter) {
            @Override
            public void handle(Request request, Response response) throws Exception {
                filter.handle(request, response);
            }
        };
    }

    protected FilterImpl(String path, String acceptType) {
        this.path = path;
        this.acceptType = acceptType;
    }

    protected FilterImpl(String path, String acceptType, Filter filter) {
        this(path, acceptType);
        this.delegate = filter;
    }

    /**
     * Invoked when a request is made on this filter's corresponding path e.g. '/hello'
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     */
    public abstract void handle(Request request, Response response) throws Exception;

    public String getAcceptType() {
        return acceptType;
    }

    /**
     * Returns this route's path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @return the filter used to create the filter implementation
     */
    @Override
    public Object delegate() {
        return this.delegate;
    }

}