package com.uchump.prime.Metatron.Lib.SimpleHttp;

import static com.uchump.prime.Metatron.Lib.SimpleHttp.EmptyHeaders.emptyHeaders;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpDelete;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpRequestVisitor;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.MessageContent;

public class HttpDeleteMessage implements HttpDelete {

    @Override
    public MessageContent getContent() {
        return () -> "";
    }

    @Override
    public Headers getHeaders() {
        return emptyHeaders();
    }

    @Override
    public void accept(HttpRequestVisitor visitor) {
        visitor.visit(this);
    }
}