package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HostPortHttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpFields;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpScheme;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;

/**
 * Adds a missing {@code Host} header (for example, HTTP 1.0 or 2.0 requests).
 * <p>
 * The host and port may be provided in the constructor or taken from the
 * {@link Request#getServerName()} and {@link Request#getServerPort()} methods.
 * </p>
 */
public class HostHeaderCustomizer implements HttpConfiguration.Customizer
{
    private final String serverName;
    private final int serverPort;

    /**
     * Construct customizer that uses {@link Request#getServerName()} and
     * {@link Request#getServerPort()} to create a host header.
     */
    public HostHeaderCustomizer()
    {
        this(null, 0);
    }

    /**
     * @param serverName the {@code serverName} to set on the request (the {@code serverPort} will not be set)
     */
    public HostHeaderCustomizer(String serverName)
    {
        this(serverName, 0);
    }

    /**
     * @param serverName the {@code serverName} to set on the request
     * @param serverPort the {@code serverPort} to set on the request
     */
    public HostHeaderCustomizer(String serverName, int serverPort)
    {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void customize(Connector connector, HttpConfiguration channelConfig, Request request)
    {
        if (request.getHttpVersion() != HttpVersion.HTTP_1_1 && !request.getHttpFields().contains(HttpHeader.HOST))
        {
            String host = serverName == null ? request.getServerName() : serverName;
            int port = HttpScheme.normalizePort(request.getScheme(), serverPort == 0 ? request.getServerPort() : serverPort);

            if (serverName != null || serverPort > 0)
                request.setHttpURI(HttpURI.build(request.getHttpURI()).authority(host, port));

            HttpFields original = request.getHttpFields();
            HttpFields.Mutable httpFields = HttpFields.build(original.size() + 1);
            httpFields.add(new HostPortHttpField(host, port));
            httpFields.add(request.getHttpFields());
            request.setHttpFields(httpFields);
        }
    }
}