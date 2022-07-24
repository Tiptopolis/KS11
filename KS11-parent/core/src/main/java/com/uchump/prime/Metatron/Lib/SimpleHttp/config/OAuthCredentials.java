package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
import java.net.URL;

public class OAuthCredentials implements AuthorisationCredentials {

    private final AccessToken token;
    private final URL url;

    public static OAuthCredentials oAuth(AccessToken token, URL url) {
        return new OAuthCredentials(token, url);
    }

    private OAuthCredentials(AccessToken token, URL url) {
        this.token = token;
        this.url = url;
    }

    @Override
    public void applyTo(ConfigurableHttpClient client) {
        client.withOAuthCredentials(token.value, url);
    }
}