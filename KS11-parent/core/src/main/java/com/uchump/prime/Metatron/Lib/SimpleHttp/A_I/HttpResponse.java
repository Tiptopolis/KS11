package com.uchump.prime.Metatron.Lib.SimpleHttp.A_I;
public interface HttpResponse extends HttpMessage {

    int getStatusCode();
    String getStatusMessage();
    String getOriginatingUri();
    boolean ok();

}