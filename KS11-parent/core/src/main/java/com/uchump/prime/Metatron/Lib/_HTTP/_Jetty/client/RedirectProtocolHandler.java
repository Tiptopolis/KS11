package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Result;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;

/**
 * <p>A protocol handler that handles redirect status codes 301, 302, 303, 307 and 308.</p>
 */
public class RedirectProtocolHandler extends Response.Listener.Adapter implements ProtocolHandler
{
    public static final String NAME = "redirect";
    
    private final HttpRedirector redirector;

    public RedirectProtocolHandler(HttpClient client)
    {
        redirector = new HttpRedirector(client);
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public boolean accept(Request request, Response response)
    {
        return redirector.isRedirect(response) && request.isFollowRedirects();
    }

    @Override
    public Response.Listener getResponseListener()
    {
        return this;
    }

    @Override
    public boolean onHeader(Response response, HttpField field)
    {
        // Avoid that the content is decoded, which could generate
        // errors, since we are discarding the content anyway.
        return field.getHeader() != HttpHeader.CONTENT_ENCODING;
    }

    @Override
    public void onComplete(Result result)
    {
        Request request = result.getRequest();
        Response response = result.getResponse();
        if (result.isSucceeded())
            redirector.redirect(request, response, null);
        else
            redirector.fail(request, response, result.getFailure());
    }
}