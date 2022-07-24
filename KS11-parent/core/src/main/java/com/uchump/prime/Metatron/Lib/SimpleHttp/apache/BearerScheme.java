package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.Credentials;
import org.apache.http.impl.auth.RFC2617Scheme;
import org.apache.http.message.BasicHeader;

public class BearerScheme extends RFC2617Scheme {

    @Override
    public String getSchemeName() {
        return "bearer";
    }

    @Override
    public boolean isConnectionBased() {
        return false;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public Header authenticate(Credentials credentials, HttpRequest request) {
        return new BasicHeader("Authorization", "Bearer " + credentials.getUserPrincipal().getName());
    }
}