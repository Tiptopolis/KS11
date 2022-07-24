package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class DummyEndpoint extends Endpoint
{
    @Override
    public void onOpen(Session session, EndpointConfig config)
    {
    }
}