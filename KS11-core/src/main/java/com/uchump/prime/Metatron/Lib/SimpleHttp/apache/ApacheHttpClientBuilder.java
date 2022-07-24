package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;



import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Builder;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.apache.http.auth.AuthScope.ANY_REALM;
import static org.apache.http.auth.AuthScope.ANY_SCHEME;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.config.HttpTimeout.httpTimeout;

public class ApacheHttpClientBuilder implements Builder<org.apache.http.client.HttpClient>, ConfigurableHttpClient {

    private Ssl ssl = Ssl.enabled;
    private List<AuthenticatedHost> authentications = new ArrayList<>();
    private Setting<URL> proxy = new NoProxy();
    private Setting<Integer> timeout = httpTimeout(of(10, MINUTES));
    private Setting<Boolean> handleRedirects = AutomaticRedirectHandling.on();

    public static ApacheHttpClientBuilder anApacheClientWithShortTimeout() {
        return new ApacheHttpClientBuilder().with(httpTimeout(of(5, SECONDS)));
    }

    @Override
    public ApacheHttpClientBuilder withBasicAuthCredentials(String username, String password, URL url) {
        this.authentications.add(new AuthenticatedHost(url, new UsernamePasswordCredentials(username, password)));
        return this;
    }

    @Override
    public ConfigurableHttpClient withOAuthCredentials(String accessToken, URL url) {
        this.authentications.add(new AuthenticatedHost(url, new OAuthAccessToken(accessToken)));
        return this;
    }

    public ApacheHttpClientBuilder with(HttpTimeout timeout) {
        this.timeout = timeout;
        return this;
    }

    public ApacheHttpClientBuilder with(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public ApacheHttpClientBuilder with(Ssl ssl) {
        this.ssl = ssl;
        return this;
    }

    public ApacheHttpClientBuilder with(AutomaticRedirectHandling handleRedirects) {
        this.handleRedirects = handleRedirects;
        return this;
    }

    public org.apache.http.client.HttpClient build() {
        return HttpClientBuilder
            .create()
            .setConnectionManager(new PoolingHttpClientConnectionManager(createSchemeRegistry()))
            .setDefaultRequestConfig(configureApache().requestConfig())
            .setDefaultCredentialsProvider(setupAuthentication())
            .build();
    }

    private Registry<ConnectionSocketFactory> createSchemeRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", ssl.getSocketFactory())
            .build();
    }

    private ApacheConfig configureApache() {
        RequestConfig.Builder config = RequestConfig.custom()
            .setCircularRedirectsAllowed(true)
            .setAuthenticationEnabled(true)
            .setExpectContinueEnabled(true);

        handleRedirects.applyTo(config::setRedirectsEnabled);
        timeout.applyTo(config::setConnectTimeout);
        timeout.applyTo(config::setConnectionRequestTimeout);
        timeout.applyTo(config::setSocketTimeout);
        proxy.applyTo(url -> config.setProxy(new HttpHost(url.getHost(), url.getPort(), url.getProtocol())));

        return new ApacheConfig(config.build());
    }
    
    private CredentialsProvider setupAuthentication() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        for (AuthenticatedHost authentication : authentications)
            credentialsProvider.setCredentials(authentication.scope, authentication.credentials);
        return credentialsProvider;
    }

    public final class AuthenticatedHost {

        private final Credentials credentials;
        private final AuthScope scope;

        public AuthenticatedHost(URL url, Credentials credentials) {
            this.credentials = credentials;
            this.scope = new AuthScope(url.getHost(), url.getPort(), ANY_REALM, ANY_SCHEME);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AuthenticatedHost that = (AuthenticatedHost) o;
            return Objects.equals(credentials, that.credentials) &&
                Objects.equals(scope, that.scope);
        }

        @Override
        public int hashCode() {
            return Objects.hash(credentials, scope);
        }

        @Override
        public String toString() {
            return format("%s{credentials='%s', scope='%s'}", this.getClass().getSimpleName(), credentials, scope);
        }
    }
}