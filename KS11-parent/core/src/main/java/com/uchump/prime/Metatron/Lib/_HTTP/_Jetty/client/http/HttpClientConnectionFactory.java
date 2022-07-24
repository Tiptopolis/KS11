package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.http;


import java.util.List;
import java.util.Map;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ClientConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;


public class HttpClientConnectionFactory implements ClientConnectionFactory
{
    /**
     * <p>Representation of the {@code HTTP/1.1} application protocol used by {@link HttpClientTransportDynamic}.</p>
     */
    public static final Info HTTP11 = new HTTP11(new HttpClientConnectionFactory());

    @Override
    public com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection newConnection(EndPoint endPoint, Map<String, Object> context)
    {
        HttpConnectionOverHTTP connection = new HttpConnectionOverHTTP(endPoint, context);
        return customize(connection, context);
    }

    private static class HTTP11 extends Info
    {
        private static final List<String> protocols = List.of("http/1.1");

        private HTTP11(ClientConnectionFactory factory)
        {
            super(factory);
        }

        @Override
        public List<String> getProtocols(boolean secure)
        {
            return protocols;
        }

        @Override
        public String toString()
        {
            return String.format("%s@%x%s", getClass().getSimpleName(), hashCode(), protocols);
        }
    }
}