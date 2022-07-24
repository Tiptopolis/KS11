package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;

/**
 * <p>A destination for those transports that are multiplex (e.g. HTTP/2).</p>
 * <p>Transports that negotiate the protocol, and therefore do not know in advance
 * whether they are duplex or multiplex, should use this class and when the
 * cardinality is known call {@link #setMaxRequestsPerConnection(int)} with
 * the proper cardinality.</p>
 * <p>If the cardinality is {@code 1}, the behavior of this class is similar
 * to that of {@link DuplexHttpDestination}.</p>
 */
public class MultiplexHttpDestination extends HttpDestination implements HttpDestination.Multiplexed
{
    public MultiplexHttpDestination(HttpClient client, Origin origin)
    {
        this(client, origin, false);
    }

    public MultiplexHttpDestination(HttpClient client, Origin origin, boolean intrinsicallySecure)
    {
        super(client, origin, intrinsicallySecure);
    }

    @ManagedAttribute(value = "The maximum number of concurrent requests per connection")
    public int getMaxRequestsPerConnection()
    {
        ConnectionPool connectionPool = getConnectionPool();
        if (connectionPool instanceof AbstractConnectionPool)
            return ((AbstractConnectionPool)connectionPool).getMaxMultiplex();
        return 1;
    }

    public void setMaxRequestsPerConnection(int maxRequestsPerConnection)
    {
        ConnectionPool connectionPool = getConnectionPool();
        if (connectionPool instanceof AbstractConnectionPool)
            ((AbstractConnectionPool)connectionPool).setMaxMultiplex(maxRequestsPerConnection);
    }
}