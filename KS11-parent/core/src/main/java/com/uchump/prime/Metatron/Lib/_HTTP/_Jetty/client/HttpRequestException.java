package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;

public class HttpRequestException extends RuntimeException
{
    private final Request request;

    public HttpRequestException(String message, Request request)
    {
        super(message);
        this.request = request;
    }

    public Request getRequest()
    {
        return request;
    }
}