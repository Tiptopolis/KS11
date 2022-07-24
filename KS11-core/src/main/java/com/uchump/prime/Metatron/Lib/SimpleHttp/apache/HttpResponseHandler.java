package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.ResponseHandler;

import com.uchump.prime.Metatron.Lib.SimpleHttp.StringHttpResponse;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpResponse;

import java.io.IOException;

import static com.uchump.prime.Metatron.Lib.SimpleHttp.apache.Coercions.asHeaders;

class HttpResponseHandler implements ResponseHandler<HttpResponse> {

    private final String originatingUri;
    private final ContentConsumingStrategy consumer;

    HttpResponseHandler(HttpRequest originatingRequest, ContentConsumingStrategy consumer) {
        this.originatingUri = originatingRequest.getRequestLine().getUri();
        this.consumer = consumer;
    }

    @Override
    public HttpResponse handleResponse(org.apache.http.HttpResponse response) throws IOException {
        return new StringHttpResponse(getStatusCodeFrom(response), getStatusMessageFrom(response), getContentFrom(response), getHeadersFrom(response), originatingUri);
    }

    private static String getStatusMessageFrom(org.apache.http.HttpResponse response) {
        return response.getStatusLine().getReasonPhrase();
    }

    private static int getStatusCodeFrom(org.apache.http.HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    private static Headers getHeadersFrom(org.apache.http.HttpResponse response) {
        return asHeaders(response.getAllHeaders());
    }

    private String getContentFrom(org.apache.http.HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null)
            return "";
        return consumer.toString(entity);
    }

}