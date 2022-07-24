package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ManagedSelector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.NetworkTrafficListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.NetworkTrafficSocketChannelEndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.SocketChannelEndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ssl.SslContextFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.Scheduler;

/**
 * <p>A specialized version of {@link ServerConnector} that supports {@link NetworkTrafficListener}s.</p>
 * <p>A {@link NetworkTrafficListener} can be set and unset dynamically before and after this connector has
 * been started.</p>
 */
public class NetworkTrafficServerConnector extends ServerConnector
{
    private volatile NetworkTrafficListener listener;

    public NetworkTrafficServerConnector(Server server)
    {
        this(server, null, null, null, 0, 0, new HttpConnectionFactory());
    }

    public NetworkTrafficServerConnector(Server server, ConnectionFactory connectionFactory, SslContextFactory.Server sslContextFactory)
    {
        super(server, sslContextFactory, connectionFactory);
    }

    public NetworkTrafficServerConnector(Server server, ConnectionFactory connectionFactory)
    {
        super(server, connectionFactory);
    }

    public NetworkTrafficServerConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool pool, int acceptors, int selectors, ConnectionFactory... factories)
    {
        super(server, executor, scheduler, pool, acceptors, selectors, factories);
    }

    public NetworkTrafficServerConnector(Server server, SslContextFactory.Server sslContextFactory)
    {
        super(server, sslContextFactory);
    }

    /**
     * @param listener the listener to set, or null to unset
     */
    public void setNetworkTrafficListener(NetworkTrafficListener listener)
    {
        this.listener = listener;
    }

    /**
     * @return the listener
     */
    public NetworkTrafficListener getNetworkTrafficListener()
    {
        return listener;
    }

    @Override
    protected SocketChannelEndPoint newEndPoint(SocketChannel channel, ManagedSelector selectSet, SelectionKey key)
    {
        return new NetworkTrafficSocketChannelEndPoint(channel, selectSet, key, getScheduler(), getIdleTimeout(), getNetworkTrafficListener());
    }
}