package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.impl;

import java.net.HttpCookie;
import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.MultiMap;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.UrlEncoded;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.UpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.CoreClientUpgradeRequest;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Representing the Jetty {@link org.eclipse.jetty.client.HttpClient}'s {@link org.eclipse.jetty.client.HttpRequest}
 * in the {@link UpgradeRequest} interface.
 */
public class DelegatedJettyClientUpgradeRequest implements UpgradeRequest
{
    private final CoreClientUpgradeRequest delegate;

    public DelegatedJettyClientUpgradeRequest(CoreClientUpgradeRequest delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public List<HttpCookie> getCookies()
    {
        return delegate.getCookies();
    }

    @Override
    public String getHeader(String name)
    {
        return delegate.getHeaders().get(name);
    }

    @Override
    public int getHeaderInt(String name)
    {
        HttpField field = delegate.getHeaders().getField(name);
        if (field == null)
            return -1;
        return field.getIntValue();
    }

    @Override
    public List<String> getHeaders(String name)
    {
        return delegate.getHeaders().getValuesList(name);
    }

    @Override
    public Map<String, List<String>> getHeaders()
    {
        return null;
    }

    @Override
    public String getHost()
    {
        return delegate.getHost();
    }

    @Override
    public String getHttpVersion()
    {
        return delegate.getVersion().toString();
    }

    public void configure(EndPoint endpoint)
    {
    }

    @Override
    public String getMethod()
    {
        return delegate.getMethod();
    }

    @Override
    public String getOrigin()
    {
        return delegate.getHeaders().get(HttpHeader.ORIGIN);
    }

    @Override
    public Map<String, List<String>> getParameterMap()
    {
        if (getQueryString() == null)
            return Collections.emptyMap();

        MultiMap<String> params = new MultiMap<>();
        UrlEncoded.decodeTo(getQueryString(), params, UTF_8);
        return params;
    }

    @Override
    public String getProtocolVersion()
    {
        return delegate.getHeaders().get(HttpHeader.SEC_WEBSOCKET_VERSION);
    }

    @Override
    public String getQueryString()
    {
        return delegate.getQuery();
    }

    @Override
    public URI getRequestURI()
    {
        return delegate.getURI();
    }

    @Override
    public List<String> getSubProtocols()
    {
        return delegate.getHeaders().getValuesList(HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL);
    }

    @Override
    public boolean hasSubProtocol(String test)
    {
        HttpField field = delegate.getHeaders().getField(HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL);
        if (field == null)
            return false;
        return field.contains(test);
    }

    @Override
    public boolean isSecure()
    {
        return HttpClient.isSchemeSecure(delegate.getScheme());
    }

    @Override
    public Principal getUserPrincipal()
    {
        return null;
    }

    @Override
    public List<ExtensionConfig> getExtensions()
    {
        List<String> rawExtensions = delegate.getHeaders().getValuesList(HttpHeader.SEC_WEBSOCKET_EXTENSIONS);
        if (rawExtensions == null || rawExtensions.isEmpty())
            return Collections.emptyList();

        return rawExtensions.stream().map(ExtensionConfig::parse).collect(Collectors.toList());
    }
}