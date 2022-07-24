package com.uchump.prime.Metatron.Lib.SimpleHttp;

import java.net.URL;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.CommonHttpClient;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpPost;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpPut;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpResponse;
import com.uchump.prime.Metatron.Lib.SimpleHttp.apache.ApacheAuthenticationSchemeHttpContextBuilder;
import com.uchump.prime.Metatron.Lib.SimpleHttp.apache.ApacheHttpClient;
import com.uchump.prime.Metatron.Lib.SimpleHttp.apache.ApacheHttpClientBuilder;
import com.uchump.prime.Metatron.Lib.SimpleHttp.apache.Ssl;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.AuthorisationCredentials;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.AutomaticRedirectHandling;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.HttpTimeout;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.Proxy;

import static com.uchump.prime.Metatron.Lib.SimpleHttp.apache.ApacheAuthenticationSchemeHttpContextBuilder.anApacheBasicAuthScheme;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.apache.ApacheHttpClientBuilder.anApacheClientWithShortTimeout;

public class HttpClients {

    private HttpClients() { }

    public static CommonHttpClient anApacheClient() {
        return new ApacheCommonHttpClient();
    }

    private static class ApacheCommonHttpClient implements CommonHttpClient {

        private final ApacheHttpClientBuilder apache = anApacheClientWithShortTimeout();
        private final ApacheAuthenticationSchemeHttpContextBuilder authenticationSchemes = anApacheBasicAuthScheme();

        private ApacheHttpClient httpClient;

        @Override
        public CommonHttpClient with(AuthorisationCredentials credentials) {
            credentials.applyTo(apache);
            credentials.applyTo(authenticationSchemes);
            return this;
        }

        @Override
        public CommonHttpClient with(HttpTimeout timeout) {
            apache.with(timeout);
            return this;
        }

        @Override
        public CommonHttpClient withoutSsl() {
            apache.with(Ssl.disabled);
            return this;
        }

        @Override
        public CommonHttpClient withTrustingSsl() {
            apache.with(Ssl.naive);
            return this;
        }

        @Override
        public CommonHttpClient with(AutomaticRedirectHandling handleRedirects) {
            apache.with(handleRedirects);
            return this;
        }

        @Override
        public CommonHttpClient with(Proxy proxy) {
            apache.with(proxy);
            return this;
        }

        @Override
        public HttpResponse get(URL url) throws HttpException {
            initialiseHttpClient();
            return httpClient.get(url);
        }

        @Override
        public HttpResponse get(URL url, Headers headers) throws HttpException {
            initialiseHttpClient();
            return httpClient.get(url, headers);
        }

        @Override
        public HttpResponse post(URL url, HttpPost message) throws HttpException {
            initialiseHttpClient();
            return httpClient.post(url, message);
        }

        @Override
        public HttpResponse put(URL url, HttpPut message) throws HttpException {
            initialiseHttpClient();
            return httpClient.put(url, message);
        }

        @Override
        public HttpResponse delete(URL url) throws HttpException {
            initialiseHttpClient();
            return httpClient.delete(url);
        }

        @Override
        public HttpResponse options(URL url) throws HttpException {
            initialiseHttpClient();
            return httpClient.options(url);
        }

        @Override
        public void shutdown() {
            initialiseHttpClient();
            httpClient.shutdown();
        }

        private void initialiseHttpClient() {
            if (httpClient == null)
                httpClient = new ApacheHttpClient(apache, authenticationSchemes);
        }
    }

}