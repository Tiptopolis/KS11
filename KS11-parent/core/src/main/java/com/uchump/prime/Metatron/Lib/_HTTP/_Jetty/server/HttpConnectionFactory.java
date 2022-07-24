package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.util.Objects;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.Name;

/**
 * A Connection Factory for HTTP Connections.
 * <p>Accepts connections either directly or via SSL and/or ALPN chained connection factories.  The accepted
 * {@link HttpConnection}s are configured by a {@link HttpConfiguration} instance that is either created by
 * default or passed in to the constructor.
 */
public class HttpConnectionFactory extends AbstractConnectionFactory implements HttpConfiguration.ConnectionFactory
{
    private final HttpConfiguration _config;
    private boolean _recordHttpComplianceViolations;
    private boolean _useInputDirectByteBuffers;
    private boolean _useOutputDirectByteBuffers;

    public HttpConnectionFactory()
    {
        this(new HttpConfiguration());
    }

    public HttpConnectionFactory(@Name("config") HttpConfiguration config)
    {
        super(HttpVersion.HTTP_1_1.asString());
        _config = Objects.requireNonNull(config);
        addBean(_config);
        setUseInputDirectByteBuffers(_config.isUseInputDirectByteBuffers());
        setUseOutputDirectByteBuffers(_config.isUseOutputDirectByteBuffers());
    }

    @Override
    public HttpConfiguration getHttpConfiguration()
    {
        return _config;
    }

    public boolean isRecordHttpComplianceViolations()
    {
        return _recordHttpComplianceViolations;
    }

    public void setRecordHttpComplianceViolations(boolean recordHttpComplianceViolations)
    {
        this._recordHttpComplianceViolations = recordHttpComplianceViolations;
    }

    public boolean isUseInputDirectByteBuffers()
    {
        return _useInputDirectByteBuffers;
    }

    public void setUseInputDirectByteBuffers(boolean useInputDirectByteBuffers)
    {
        _useInputDirectByteBuffers = useInputDirectByteBuffers;
    }

    public boolean isUseOutputDirectByteBuffers()
    {
        return _useOutputDirectByteBuffers;
    }

    public void setUseOutputDirectByteBuffers(boolean useOutputDirectByteBuffers)
    {
        _useOutputDirectByteBuffers = useOutputDirectByteBuffers;
    }

    @Override
    public Connection newConnection(Connector connector, EndPoint endPoint)
    {
        HttpConnection connection = new HttpConnection(_config, connector, endPoint, isRecordHttpComplianceViolations());
        connection.setUseInputDirectByteBuffers(isUseInputDirectByteBuffers());
        connection.setUseOutputDirectByteBuffers(isUseOutputDirectByteBuffers());
        return configure(connection, connector, endPoint);
    }
}