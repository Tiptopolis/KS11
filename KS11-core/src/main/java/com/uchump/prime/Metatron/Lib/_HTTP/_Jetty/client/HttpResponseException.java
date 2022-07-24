package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;

public class HttpResponseException extends RuntimeException
{
    private final Response response;

    public HttpResponseException(String message, Response response)
    {
        this(message, response, null);
    }

    public HttpResponseException(String message, Response response, Throwable cause)
    {
        super(message, cause);
        this.response = response;
    }

    public Response getResponse()
    {
        return response;
    }
}