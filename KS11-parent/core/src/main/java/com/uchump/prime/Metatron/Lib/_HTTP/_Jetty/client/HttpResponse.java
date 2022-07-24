package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpFields;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;


public class HttpResponse implements Response
{
    private final HttpFields.Mutable headers = HttpFields.build();
    private final Request request;
    private final List<ResponseListener> listeners;
    private HttpVersion version;
    private int status;
    private String reason;
    private HttpFields.Mutable trailers;

    public HttpResponse(Request request, List<ResponseListener> listeners)
    {
        this.request = request;
        this.listeners = listeners;
    }

    @Override
    public Request getRequest()
    {
        return request;
    }

    @Override
    public HttpVersion getVersion()
    {
        return version;
    }

    public HttpResponse version(HttpVersion version)
    {
        this.version = version;
        return this;
    }

    @Override
    public int getStatus()
    {
        return status;
    }

    public HttpResponse status(int status)
    {
        this.status = status;
        return this;
    }

    @Override
    public String getReason()
    {
        return reason;
    }

    public HttpResponse reason(String reason)
    {
        this.reason = reason;
        return this;
    }

    @Override
    public HttpFields getHeaders()
    {
        return headers.asImmutable();
    }

    public void clearHeaders()
    {
        headers.clear();
    }

    public HttpResponse addHeader(HttpField header)
    {
        headers.add(header);
        return this;
    }

    public HttpResponse headers(Consumer<HttpFields.Mutable> consumer)
    {
        consumer.accept(headers);
        return this;
    }

    @Override
    public <T extends ResponseListener> List<T> getListeners(Class<T> type)
    {
        ArrayList<T> result = new ArrayList<>();
        for (ResponseListener listener : listeners)
        {
            if (type == null || type.isInstance(listener))
                result.add((T)listener);
        }
        return result;
    }

    public HttpFields getTrailers()
    {
        return trailers == null ? null : trailers.asImmutable();
    }

    public HttpResponse trailer(HttpField trailer)
    {
        if (trailers == null)
            trailers = HttpFields.build();
        trailers.add(trailer);
        return this;
    }

    @Override
    public boolean abort(Throwable cause)
    {
        return request.abort(cause);
    }

    @Override
    public String toString()
    {
        return String.format("%s[%s %d %s]@%x", HttpResponse.class.getSimpleName(), getVersion(), getStatus(), getReason(), hashCode());
    }
}