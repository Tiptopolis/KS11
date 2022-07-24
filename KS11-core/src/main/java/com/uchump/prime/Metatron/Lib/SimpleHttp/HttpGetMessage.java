package com.uchump.prime.Metatron.Lib.SimpleHttp;

import static com.uchump.prime.Metatron.Lib.SimpleHttp.EmptyHeaders.emptyHeaders;


import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpGet;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpRequestVisitor;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.MessageContent;

public class HttpGetMessage implements HttpGet {

    private final Headers headers;

    public HttpGetMessage() {
        this.headers = emptyHeaders();
    }

    public HttpGetMessage(Headers headers) {
        this.headers = headers;
    }

    @Override
    public void accept(HttpRequestVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public MessageContent getContent() {
        return () -> "";
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }
}