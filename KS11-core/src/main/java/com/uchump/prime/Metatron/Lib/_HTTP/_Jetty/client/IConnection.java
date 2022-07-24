package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Connection;

public interface IConnection extends Connection
{
    public SendFailure send(HttpExchange exchange);
}