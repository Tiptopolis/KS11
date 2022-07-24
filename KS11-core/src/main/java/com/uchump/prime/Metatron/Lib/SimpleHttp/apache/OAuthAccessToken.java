package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;
import org.apache.http.auth.Credentials;

import java.security.Principal;

public class OAuthAccessToken implements Credentials {

    private final String token;

    public OAuthAccessToken(String token) {
        this.token = token;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> token;
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("OAuth access tokens use the principle to represent the token and so have no password");
    }
}