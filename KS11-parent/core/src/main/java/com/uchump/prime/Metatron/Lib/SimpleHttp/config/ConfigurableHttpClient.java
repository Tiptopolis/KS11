package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
import java.net.URL;

public interface ConfigurableHttpClient {

    ConfigurableHttpClient withBasicAuthCredentials(String username, String password, URL url);

    ConfigurableHttpClient withOAuthCredentials(String accessToken, URL url);

}