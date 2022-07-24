package com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLEngine;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ClientConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.NegotiatingClientConnection;

public class ALPNClientConnection extends NegotiatingClientConnection
{
    private final List<String> protocols;

    public ALPNClientConnection(EndPoint endPoint, Executor executor, ClientConnectionFactory connectionFactory, SSLEngine sslEngine, Map<String, Object> context, List<String> protocols)
    {
        super(endPoint, executor, sslEngine, connectionFactory, context);
        this.protocols = protocols;
    }

    public List<String> getProtocols()
    {
        return protocols;
    }

    public void selected(String protocol)
    {
        completed(protocol);
    }
}