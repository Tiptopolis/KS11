package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.jmx;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.ObjectMBean;

public class HttpClientMBean extends ObjectMBean
{
    public HttpClientMBean(Object managedObject)
    {
        super(managedObject);
    }

    @Override
    public String getObjectContextBasis()
    {
        // Returning the HttpClient name as the "context" property
        // because it is inherited by the ObjectNames of the components
        // of HttpClient such as the transport, the threadpool, etc.
        HttpClient httpClient = (HttpClient)getManagedObject();
        return httpClient.getName();
    }
}