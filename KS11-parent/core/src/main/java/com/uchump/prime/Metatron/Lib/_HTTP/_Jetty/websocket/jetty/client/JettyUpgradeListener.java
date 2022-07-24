package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;

public interface JettyUpgradeListener
{
    /**
     * Event that triggers before the Handshake request is sent.
     *
     * @param request the request
     */
    default void onHandshakeRequest(HttpRequest request)
    {
    }

    /**
     * Event that triggers after the Handshake response has been received.
     *
     * @param request the request that was used
     * @param response the response that was received
     */
    default void onHandshakeResponse(HttpRequest request, HttpResponse response)
    {
    }
}