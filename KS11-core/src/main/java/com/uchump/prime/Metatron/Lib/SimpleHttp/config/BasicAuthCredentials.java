package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
import java.net.URL;

public class BasicAuthCredentials implements AuthorisationCredentials {

    private final Username username;
    private final Password password;
    private final URL url;

    public static BasicAuthCredentials basicAuth(Username username, Password password, URL url) {
        return new BasicAuthCredentials(username, password, url);
    }

    private BasicAuthCredentials(Username username, Password password, URL url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    @Override
    public void applyTo(ConfigurableHttpClient client) {
        client.withBasicAuthCredentials(username.value, password.value, url);
    }
}