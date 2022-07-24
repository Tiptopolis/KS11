package com.uchump.prime.Metatron.Lib.SimpleHttp.config;

import java.net.URL;
import java.util.Optional;

import static java.lang.String.format;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.Url.url;
import static org.apache.http.util.TextUtils.isEmpty;

import com.uchump.prime.Metatron.Lib.SimpleHttp.Url;

public class SystemPropertyProxy extends Proxy {

    private static final String proxyUrl = "http.proxyHost";
    private static final String port = "http.proxyPort";
    private static final String user = "http.proxyUser";
    private static final String password = "http.proxyPassword";

    public static Optional<Proxy> systemPropertyProxy() {
    	
        try {
            return Optional.of(new SystemPropertyProxy(getUrlFromSystemProperties()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private SystemPropertyProxy(URL value) {
        super(value);
    }

    public static URL getUrlFromSystemProperties() {
        String url = (String) System.getProperties().get(SystemPropertyProxy.proxyUrl);
        String port = (String) System.getProperties().get(SystemPropertyProxy.port);
        if (isEmpty(url) || isEmpty(port))
            throw new IllegalArgumentException(format("'%s' and/or '%s' were not specified as system properties, use -D or set programatically", SystemPropertyProxy.proxyUrl, SystemPropertyProxy.port));
        if (url.toLowerCase().contains("://"))
            throw new IllegalArgumentException(format("no need to set a protocol on the proxy URL specified by the system property '%s'", proxyUrl));
        String username = (String) System.getProperties().get(SystemPropertyProxy.user);
        String password = (String) System.getProperties().get(SystemPropertyProxy.password);
        if (isEmpty(username) || isEmpty(password))
            return url(format("http://%s:%s", url, port));
        return url(format("http://%s:%s@%s:%s", username, password, url, port));
    }
}