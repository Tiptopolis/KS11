package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import java.util.concurrent.Callable;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpResponse;

class ApacheHttpRequestExecutor implements Callable<HttpResponse> {

    private final org.apache.http.client.HttpClient client;
    private final HttpContext localContext;
    private final HttpUriRequest request;

    public ApacheHttpRequestExecutor(org.apache.http.client.HttpClient client, HttpContext localContext, HttpUriRequest request) {
        this.request = request;
        this.client = client;
        this.localContext = localContext;
    }

    @Override
    public HttpResponse call() throws Exception {
        return client.execute(request, new HttpResponseHandler(request, new ToStringConsumer()), localContext);
    }
}