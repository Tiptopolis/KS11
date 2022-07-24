package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.internal;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.HandshakeResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeResponse;


public class JsrHandshakeResponse implements HandshakeResponse
{
    private final ServerUpgradeResponse delegate;
    private Map<String, List<String>> headerMap;

    public JsrHandshakeResponse(ServerUpgradeResponse resp)
    {
        this.delegate = resp;
        this.headerMap = new HashMap<>();
        this.headerMap.putAll(resp.getHeadersMap());
    }

    @Override
    public Map<String, List<String>> getHeaders()
    {
        return headerMap;
    }

    public void setHeaders(Map<String, List<String>> headers)
    {
        headers.forEach((key, values) -> delegate.setHeader(key, values));
    }
}