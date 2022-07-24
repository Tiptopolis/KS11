package com.uchump.prime.Metatron.Lib.SimpleHttp.A_I;

import com.uchump.prime.Metatron.Lib.SimpleHttp.config.AuthorisationCredentials;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.AutomaticRedirectHandling;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.HttpTimeout;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.Proxy;

/**
 * The common configuration interface for all {@link HttpClient} implementations.
 *
 * Clients may or may not implement the various configuration parameters and they may or may not take affect after actual
 * {@link HttpClient} calls. For example, you might set a timeout using the {@link CommonHttpClient#with(HttpTimeout)}
 * and then call the {@link HttpClient#get(java.net.URL)} to perform a HTTP GET. The underlying HTTP client implementation
 * should respect the timeout configuration but is free to ignore subsequent calls to set the timeout (ie, implementations
 * may choose to lazy load the underlying client).
 *
 */
public interface CommonHttpClient extends HttpClient {

    CommonHttpClient with(AuthorisationCredentials credentials);

    CommonHttpClient withoutSsl();

    CommonHttpClient withTrustingSsl();

    CommonHttpClient with(AutomaticRedirectHandling handleRedirects);

    CommonHttpClient with(Proxy proxy);

    CommonHttpClient with(HttpTimeout timeout);
}