package com.uchump.prime.Metatron.Lib.SimpleHttp;
import static java.lang.String.format;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpResponse;

public class StringHttpResponse implements HttpResponse {

    private final int statusCode;
    private final String statusMessage;
    private final String content;
    private final Headers headers;
    private final String originatingUri;

    public StringHttpResponse(int statusCode, String statusMessage, String content, Headers headers, String originatingUri) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.content = content;
        this.headers = headers;
        this.originatingUri = originatingUri;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public StringMessageContent getContent() {
        return new StringMessageContent(content);
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    @Override
    public String getOriginatingUri() {
        return originatingUri;
    }

    @Override
    public boolean ok() {
        return statusCode == 200;
    }

    @Override
    public String toString() {
        return format("%s{statusCode=%d, statusMessage='%s', content='%s', headers='%s', originatingUri='%s'}", this.getClass().getSimpleName(), statusCode, statusMessage, content, headers, originatingUri);
    }
}