package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.HandshakeResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpFields;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.UpgradeListener;

public class JsrUpgradeListener implements UpgradeListener
{
    private final Configurator configurator;

    public JsrUpgradeListener(Configurator configurator)
    {
        this.configurator = configurator;
    }

    @Override
    public void onHandshakeRequest(HttpRequest request)
    {
        if (configurator == null)
            return;

        HttpFields fields = request.getHeaders();
        Map<String, List<String>> originalHeaders = new HashMap<>();
        fields.forEach(field ->
        {
            originalHeaders.putIfAbsent(field.getName(), new ArrayList<>());
            List<String> values = originalHeaders.get(field.getName());
            Collections.addAll(values, field.getValues());
        });

        // Give headers to configurator
        configurator.beforeRequest(originalHeaders);

        // Reset headers on HttpRequest per configurator
        request.headers(headers ->
        {
            headers.clear();
            originalHeaders.forEach(headers::put);
        });
    }

    @Override
    public void onHandshakeResponse(HttpRequest request, HttpResponse response)
    {
        if (configurator == null)
            return;

        HandshakeResponse handshakeResponse = () ->
        {
            Map<String, List<String>> ret = new HashMap<>();
            response.getHeaders().forEach(field ->
            {
                ret.putIfAbsent(field.getName(), new ArrayList<>());
                List<String> values = ret.get(field.getName());
                Collections.addAll(values, field.getValues());
            });
            return ret;
        };

        configurator.afterResponse(handshakeResponse);
    }
}