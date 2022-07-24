package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.AbstractConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ArrayUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ssl.SslContextFactory;

/**
 * <p>Provides the common handling for {@link ConnectionFactory} implementations.</p>
 */
@ManagedObject
public abstract class AbstractConnectionFactory extends ContainerLifeCycle implements ConnectionFactory
{
    private final String _protocol;
    private final List<String> _protocols;
    private int _inputbufferSize = 8192;

    protected AbstractConnectionFactory(String protocol)
    {
        _protocol = protocol;
        _protocols = Collections.unmodifiableList(Arrays.asList(new String[]{protocol}));
    }

    protected AbstractConnectionFactory(String... protocols)
    {
        _protocol = protocols[0];
        _protocols = Collections.unmodifiableList(Arrays.asList(protocols));
    }

    @Override
    @ManagedAttribute(value = "The protocol name", readonly = true)
    public String getProtocol()
    {
        return _protocol;
    }

    @Override
    public List<String> getProtocols()
    {
        return _protocols;
    }

    @ManagedAttribute("The buffer size used to read from the network")
    public int getInputBufferSize()
    {
        return _inputbufferSize;
    }

    public void setInputBufferSize(int size)
    {
        _inputbufferSize = size;
    }

    protected String findNextProtocol(Connector connector)
    {
        return findNextProtocol(connector, getProtocol());
    }

    protected static String findNextProtocol(Connector connector, String currentProtocol)
    {
        String nextProtocol = null;
        for (Iterator<String> it = connector.getProtocols().iterator(); it.hasNext(); )
        {
            String protocol = it.next();
            if (currentProtocol.equalsIgnoreCase(protocol))
            {
                nextProtocol = it.hasNext() ? it.next() : null;
                break;
            }
        }
        return nextProtocol;
    }

    protected AbstractConnection configure(AbstractConnection connection, Connector connector, EndPoint endPoint)
    {
        connection.setInputBufferSize(getInputBufferSize());

        // Add Connection.Listeners from Connector
        connector.getEventListeners().forEach(connection::addEventListener);

        // Add Connection.Listeners from this factory
        getEventListeners().forEach(connection::addEventListener);

        return connection;
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x%s", this.getClass().getSimpleName(), hashCode(), getProtocols());
    }

    public static ConnectionFactory[] getFactories(SslContextFactory.Server sslContextFactory, ConnectionFactory... factories)
    {
        factories = ArrayUtil.removeNulls(factories);

        if (sslContextFactory == null)
            return factories;

        for (ConnectionFactory factory : factories)
        {
            if (factory instanceof HttpConfiguration.ConnectionFactory)
            {
                HttpConfiguration config = ((HttpConfiguration.ConnectionFactory)factory).getHttpConfiguration();
                if (config.getCustomizer(SecureRequestCustomizer.class) == null)
                    config.addCustomizer(new SecureRequestCustomizer());
            }
        }
        return ArrayUtil.prependToArray(new SslConnectionFactory(sslContextFactory, factories[0].getProtocol()), factories, ConnectionFactory.class);
    }
}