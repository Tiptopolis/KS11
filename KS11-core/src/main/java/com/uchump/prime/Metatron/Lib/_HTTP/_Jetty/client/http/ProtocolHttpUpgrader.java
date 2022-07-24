package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClientTransport;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpDestination;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponseException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpUpgrader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.Origin;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.dynamic.HttpClientTransportDynamic;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Promise;

/**
 * <p>A HttpUpgrader that upgrades to a given protocol.</p>
 * <p>Works in conjunction with {@link HttpClientTransportDynamic}
 * so that the protocol to upgrade to must be one of the application
 * protocols supported by HttpClientTransportDynamic.</p>
 * <p></p>
 */
public class ProtocolHttpUpgrader implements HttpUpgrader
{
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolHttpUpgrader.class);

    private final HttpDestination destination;
    private final String protocol;

    public ProtocolHttpUpgrader(HttpDestination destination, String protocol)
    {
        this.destination = destination;
        this.protocol = protocol;
    }

    @Override
    public void prepare(HttpRequest request)
    {
    }

    @Override
    public void upgrade(HttpResponse response, EndPoint endPoint, Callback callback)
    {
        if (response.getHeaders().contains(HttpHeader.UPGRADE, protocol))
        {
            HttpClient httpClient = destination.getHttpClient();
            HttpClientTransport transport = httpClient.getTransport();
            if (transport instanceof HttpClientTransportDynamic)
            {
                HttpClientTransportDynamic dynamicTransport = (HttpClientTransportDynamic)transport;

                Origin origin = destination.getOrigin();
                Origin newOrigin = new Origin(origin.getScheme(), origin.getAddress(), origin.getTag(), new Origin.Protocol(List.of(protocol), false));
                HttpDestination newDestination = httpClient.resolveDestination(newOrigin);

                Map<String, Object> context = new HashMap<>();
                context.put(HttpClientTransport.HTTP_DESTINATION_CONTEXT_KEY, newDestination);
                context.put(HttpResponse.class.getName(), response);
                context.put(HttpClientTransport.HTTP_CONNECTION_PROMISE_CONTEXT_KEY, Promise.from(y -> callback.succeeded(), callback::failed));

                if (LOG.isDebugEnabled())
                    LOG.debug("Upgrading {} on {}", response.getRequest(), endPoint);

                dynamicTransport.upgrade(endPoint, context);
            }
            else
            {
                callback.failed(new HttpResponseException(HttpClientTransportDynamic.class.getName() + " required to upgrade to: " + protocol, response));
            }
        }
        else
        {
            callback.failed(new HttpResponseException("Not an upgrade to: " + protocol, response));
        }
    }
}