package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api;

/**
 * The result of a request / response exchange, containing the {@link Request}, the {@link Response}
 * and eventual failures of either.
 */
public class Result
{
    private final Request request;
    private final Throwable requestFailure;
    private final Response response;
    private final Throwable responseFailure;

    public Result(Request request, Response response)
    {
        this(request, null, response, null);
    }

    public Result(Request request, Response response, Throwable responseFailure)
    {
        this(request, null, response, responseFailure);
    }

    public Result(Request request, Throwable requestFailure, Response response)
    {
        this(request, requestFailure, response, null);
    }

    public Result(Request request, Throwable requestFailure, Response response, Throwable responseFailure)
    {
        this.request = request;
        this.requestFailure = requestFailure;
        this.response = response;
        this.responseFailure = responseFailure;
    }

    public Result(Result result, Throwable responseFailure)
    {
        this.request = result.request;
        this.requestFailure = result.requestFailure;
        this.response = result.response;
        this.responseFailure = responseFailure;
    }

    /**
     * @return the request object
     */
    public Request getRequest()
    {
        return request;
    }

    /**
     * @return the request failure, if any
     */
    public Throwable getRequestFailure()
    {
        return requestFailure;
    }

    /**
     * @return the response object
     */
    public Response getResponse()
    {
        return response;
    }

    /**
     * @return the response failure, if any
     */
    public Throwable getResponseFailure()
    {
        return responseFailure;
    }

    /**
     * @return whether both the request and the response succeeded
     */
    public boolean isSucceeded()
    {
        return getFailure() == null;
    }

    /**
     * @return whether either the response or the request failed
     */
    public boolean isFailed()
    {
        return !isSucceeded();
    }

    /**
     * @return the response failure, if any, otherwise the request failure, if any
     */
    public Throwable getFailure()
    {
        return responseFailure != null ? responseFailure : requestFailure;
    }

    @Override
    public String toString()
    {
        return String.format("%s[%s > %s] %s",
            Result.class.getSimpleName(),
            request,
            response,
            getFailure());
    }
}