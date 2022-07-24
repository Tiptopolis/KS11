package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

/**
 * <p>A destination for those network transports that are duplex (e.g. HTTP/1.1 and FastCGI).</p>
 *
 * @see MultiplexHttpDestination
 */
public class DuplexHttpDestination extends HttpDestination
{
    public DuplexHttpDestination(HttpClient client, Origin origin)
    {
        this(client, origin, false);
    }

    public DuplexHttpDestination(HttpClient client, Origin origin, boolean intrinsicallySecure)
    {
        super(client, origin, intrinsicallySecure);
    }
}