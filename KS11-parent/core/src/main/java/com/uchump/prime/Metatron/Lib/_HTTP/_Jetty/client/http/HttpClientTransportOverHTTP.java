package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.http;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.AbstractConnectorHttpClientTransport;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.DuplexConnectionPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.DuplexHttpDestination;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpDestination;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.Origin;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ClientConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ClientConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ProcessorUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;

@ManagedObject("The HTTP/1.1 client transport")
public class HttpClientTransportOverHTTP extends AbstractConnectorHttpClientTransport
{
    public static final Origin.Protocol HTTP11 = new Origin.Protocol(List.of("http/1.1"), false);
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientTransportOverHTTP.class);

    private final ClientConnectionFactory factory = new HttpClientConnectionFactory();
    private int headerCacheSize = 1024;
    private boolean headerCacheCaseSensitive;

    public HttpClientTransportOverHTTP()
    {
        this(Math.max(1, ProcessorUtils.availableProcessors() / 2));
    }

    public HttpClientTransportOverHTTP(int selectors)
    {
        this(new ClientConnector());
        getClientConnector().setSelectors(selectors);
    }

    public HttpClientTransportOverHTTP(ClientConnector connector)
    {
        super(connector);
        setConnectionPoolFactory(destination -> new DuplexConnectionPool(destination, getHttpClient().getMaxConnectionsPerDestination(), destination));
    }

    @Override
    public Origin newOrigin(HttpRequest request)
    {
        return getHttpClient().createOrigin(request, HTTP11);
    }

    @Override
    public HttpDestination newHttpDestination(Origin origin)
    {
        SocketAddress address = origin.getAddress().getSocketAddress();
        return new DuplexHttpDestination(getHttpClient(), origin, getClientConnector().isIntrinsicallySecure(address));
    }

    @Override
    public com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection newConnection(EndPoint endPoint, Map<String, Object> context) throws IOException
    {
        var connection = factory.newConnection(endPoint, context);
        if (LOG.isDebugEnabled())
            LOG.debug("Created {}", connection);
        return connection;
    }

    @ManagedAttribute("The maximum allowed size in bytes for an HTTP header field cache")
    public int getHeaderCacheSize()
    {
        return headerCacheSize;
    }

    public void setHeaderCacheSize(int headerCacheSize)
    {
        this.headerCacheSize = headerCacheSize;
    }

    @ManagedAttribute("Whether the header field cache is case sensitive")
    public boolean isHeaderCacheCaseSensitive()
    {
        return headerCacheCaseSensitive;
    }

    public void setHeaderCacheCaseSensitive(boolean headerCacheCaseSensitive)
    {
        this.headerCacheCaseSensitive = headerCacheCaseSensitive;
    }
}