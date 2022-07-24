package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import java.net.URI;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpStatus;

/**
 * <p>A protocol handler that handles the 401 response code
 * in association with the {@code Proxy-Authenticate} header.</p>
 *
 * @see WWWAuthenticationProtocolHandler
 */
public class ProxyAuthenticationProtocolHandler extends AuthenticationProtocolHandler
{
    public static final String NAME = "proxy-authenticate";
    private static final String ATTRIBUTE = ProxyAuthenticationProtocolHandler.class.getName() + ".attribute";

    public ProxyAuthenticationProtocolHandler(HttpClient client)
    {
        this(client, DEFAULT_MAX_CONTENT_LENGTH);
    }

    public ProxyAuthenticationProtocolHandler(HttpClient client, int maxContentLength)
    {
        super(client, maxContentLength);
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public boolean accept(Request request, Response response)
    {
        return response.getStatus() == HttpStatus.PROXY_AUTHENTICATION_REQUIRED_407;
    }

    @Override
    protected HttpHeader getAuthenticateHeader()
    {
        return HttpHeader.PROXY_AUTHENTICATE;
    }

    @Override
    protected HttpHeader getAuthorizationHeader()
    {
        return HttpHeader.PROXY_AUTHORIZATION;
    }

    @Override
    protected URI getAuthenticationURI(Request request)
    {
        HttpDestination destination = (HttpDestination)getHttpClient().resolveDestination(request);
        ProxyConfiguration.Proxy proxy = destination.getProxy();
        return proxy != null ? proxy.getURI() : request.getURI();
    }

    @Override
    protected String getAuthenticationAttribute()
    {
        return ATTRIBUTE;
    }
}