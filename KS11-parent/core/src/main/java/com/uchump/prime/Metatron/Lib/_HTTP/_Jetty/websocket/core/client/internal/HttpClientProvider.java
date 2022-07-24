package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.internal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;

public interface HttpClientProvider
{
    static HttpClient get()
    {
        HttpClientProvider xmlProvider = new XmlHttpClientProvider();
        HttpClient client = xmlProvider.newHttpClient();
        if (client != null)
            return client;

        return HttpClientProvider.newDefaultHttpClient();
    }

    private static HttpClient newDefaultHttpClient()
    {
        return new HttpClient();
    }

    default HttpClient newHttpClient()
    {
        return newDefaultHttpClient();
    }
}