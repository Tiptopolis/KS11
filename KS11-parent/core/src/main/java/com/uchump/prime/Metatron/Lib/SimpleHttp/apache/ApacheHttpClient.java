package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.protocol.HttpContext;
import com.uchump.prime.Metatron.Lib.SimpleHttp.*;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.*;

import java.net.URL;
import java.util.concurrent.Callable;

import static  com.uchump.prime.Metatron.Lib.SimpleHttp.EmptyHeaders.emptyHeaders;
import static  com.uchump.prime.Metatron.Lib.SimpleHttp.apache.Coercions.asApacheBasicHeader;

public class ApacheHttpClient implements HttpClient {

    private final org.apache.http.client.HttpClient client;
    private final HttpContext localContext;
    private final Executor<HttpException> executor;

    public ApacheHttpClient(Builder<org.apache.http.client.HttpClient> clientBuilder, Builder<HttpContext> localContextBuilder) {
        this.client = clientBuilder.build();
        this.localContext = localContextBuilder.build();
        this.executor = new ApacheExceptionWrappingExecutor();
    }

    @Override
    public HttpResponse get(URL url, Headers headers) throws HttpException {
        HttpGet get = new HttpGet(url.toExternalForm());
        get.setHeaders(asApacheBasicHeader(headers));
        return execute(get);
    }

    @Override
    public HttpResponse get(URL url) throws HttpException {
        return get(url, emptyHeaders());
    }

    @Override
    public HttpResponse post(URL url, com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpPost message) throws HttpException {
        HttpPost post = new HttpPost(url.toExternalForm());
        for (Header header : message.getHeaders())
            post.addHeader(header.name(), header.value());
        post.setEntity(new HttpRequestToEntity(message).asHttpEntity());
        return execute(post);
    }

    @Override
    public HttpResponse put(URL url, com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpPut message) {
        HttpPut put = new HttpPut(url.toExternalForm());
        for (Header header : message.getHeaders())
            put.addHeader(header.name(), header.value());
        put.setEntity(new HttpRequestToEntity(message).asHttpEntity());
        return execute(put);
    }

    @Override
    public HttpResponse delete(URL url) throws HttpException {
        return execute(new HttpDelete(url.toExternalForm()));
    }

    @Override
    public HttpResponse options(URL url) throws HttpException {
        return execute(new HttpOptions(url.toExternalForm()));
    }

    private HttpResponse execute(HttpUriRequest request) {
        return executor.submit(http(request));
    }

    private Callable<HttpResponse> http(HttpUriRequest request) {
        return new ApacheHttpRequestExecutor(client, localContext, request);
    }

    @Override
    public void shutdown() {
        client.getConnectionManager().shutdown();
    }

}