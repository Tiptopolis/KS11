package com.uchump.prime.Metatron.Lib.SimpleHttp.java;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class AlwaysTrustingDefaultSslSocketFactory {

    public void configureDefaultSslSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, new TrustManager[]{new AlwaysTrustingX509TrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private static class AlwaysTrustingX509TrustManager implements X509TrustManager {

        private static final X509Certificate[] AcceptedIssuers = new X509Certificate[]{};

        public void checkClientTrusted(X509Certificate[] chain, String authType) { /* always proceed / trust */ }

        public void checkServerTrusted(X509Certificate[] chain, String authType) { /* always proceed / trust */ }

        public X509Certificate[] getAcceptedIssuers() {
            return (AcceptedIssuers);
        }
    }

}