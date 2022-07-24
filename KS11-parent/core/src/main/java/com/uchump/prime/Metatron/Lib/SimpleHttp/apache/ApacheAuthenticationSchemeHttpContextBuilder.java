package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;



import java.net.URL;
import org.apache.http.client.AuthCache;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Builder;
import com.uchump.prime.Metatron.Lib.SimpleHttp.config.ConfigurableHttpClient;

import static org.apache.http.client.protocol.ClientContext.AUTH_CACHE;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.apache.Coercions.asHttpHost;

/**
 * Build a "local context" to use with individual HTTP verbs. The {@link HttpContext} is used to share information, in
 * this case, which authentication scheme the client should be using (based on the target URL).
 */
public class ApacheAuthenticationSchemeHttpContextBuilder implements Builder<HttpContext>, ConfigurableHttpClient {

    private final BasicHttpContext localContext = new BasicHttpContext();
    private final AuthCache authenticationSchemes = new BasicAuthCache();

    private ApacheAuthenticationSchemeHttpContextBuilder() {
    }

    public static ApacheAuthenticationSchemeHttpContextBuilder anApacheBasicAuthScheme() {
        return new ApacheAuthenticationSchemeHttpContextBuilder();
    }

    @Override
    public ApacheAuthenticationSchemeHttpContextBuilder withBasicAuthCredentials(String username, String password, URL url) {
        authenticationSchemes.put(asHttpHost(url), new BasicScheme());
        return this;
    }

    @Override
    public ApacheAuthenticationSchemeHttpContextBuilder withOAuthCredentials(String accessToken, URL url) {
        authenticationSchemes.put(asHttpHost(url), new BearerScheme());
        return this;
    }

    @Override
    public HttpContext build() {
        localContext.setAttribute(AUTH_CACHE, authenticationSchemes);
        return localContext;
    }

}