package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ClientConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Promise;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;


@ManagedObject
public abstract class AbstractConnectorHttpClientTransport extends AbstractHttpClientTransport
{
    private final ClientConnector connector;

    protected AbstractConnectorHttpClientTransport(ClientConnector connector)
    {
        this.connector = Objects.requireNonNull(connector);
        addBean(connector);
    }

    public ClientConnector getClientConnector()
    {
        return connector;
    }

    @ManagedAttribute(value = "The number of selectors", readonly = true)
    public int getSelectors()
    {
        return connector.getSelectors();
    }

    @Override
    protected void doStart() throws Exception
    {
        HttpClient httpClient = getHttpClient();
        connector.setBindAddress(httpClient.getBindAddress());
        connector.setByteBufferPool(httpClient.getByteBufferPool());
        connector.setConnectBlocking(httpClient.isConnectBlocking());
        connector.setConnectTimeout(Duration.ofMillis(httpClient.getConnectTimeout()));
        connector.setExecutor(httpClient.getExecutor());
        connector.setIdleTimeout(Duration.ofMillis(httpClient.getIdleTimeout()));
        connector.setScheduler(httpClient.getScheduler());
        connector.setSslContextFactory(httpClient.getSslContextFactory());
        super.doStart();
    }

    @Override
    public void connect(SocketAddress address, Map<String, Object> context)
    {
        HttpDestination destination = (HttpDestination)context.get(HTTP_DESTINATION_CONTEXT_KEY);
        context.put(ClientConnector.CLIENT_CONNECTION_FACTORY_CONTEXT_KEY, destination.getClientConnectionFactory());
        @SuppressWarnings("unchecked")
        Promise<Connection> promise = (Promise<Connection>)context.get(HTTP_CONNECTION_PROMISE_CONTEXT_KEY);
        context.put(ClientConnector.CONNECTION_PROMISE_CONTEXT_KEY, Promise.from(ioConnection -> {}, promise::failed));
        connector.connect(address, context);
    }

    @Override
    public void connect(InetSocketAddress address, Map<String, Object> context)
    {
        connect((SocketAddress)address, context);
    }
}