package com.uchump.prime.Metatron.Lib.SimpleHttp.A_I;
public interface HttpRequest extends HttpMessage {

    void accept(HttpRequestVisitor visitor);

}