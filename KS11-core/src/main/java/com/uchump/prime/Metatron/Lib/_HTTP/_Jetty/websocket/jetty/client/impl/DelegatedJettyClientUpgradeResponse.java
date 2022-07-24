package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.UpgradeResponse;

/**
 * Representing the Jetty {@link org.eclipse.jetty.client.HttpClient}'s {@link org.eclipse.jetty.client.HttpResponse}
 * in the {@link UpgradeResponse} interface.
 */
public class DelegatedJettyClientUpgradeResponse implements UpgradeResponse
{
    private final HttpResponse delegate;

    public DelegatedJettyClientUpgradeResponse(HttpResponse response)
    {
        this.delegate = response;
    }

    @Override
    public String getAcceptedSubProtocol()
    {
        return this.delegate.getHeaders().get(HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL);
    }

    @Override
    public String getHeader(String name)
    {
        return this.delegate.getHeaders().get(name);
    }

    @Override
    public Set<String> getHeaderNames()
    {
        return delegate.getHeaders().getFieldNamesCollection();
    }

    @Override
    public List<String> getHeaders(String name)
    {
        return this.delegate.getHeaders().getValuesList(name);
    }

    @Override
    public Map<String, List<String>> getHeaders()
    {
        Map<String, List<String>> headers = getHeaderNames().stream()
            .collect(Collectors.toMap((name) -> name, (name) -> new ArrayList<>(getHeaders(name))));
        return Collections.unmodifiableMap(headers);
    }

    @Override
    public int getStatusCode()
    {
        return this.delegate.getStatus();
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