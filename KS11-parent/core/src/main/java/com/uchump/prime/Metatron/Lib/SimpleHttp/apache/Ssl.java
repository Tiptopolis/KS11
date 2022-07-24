package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;

import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import com.uchump.prime.Metatron.Lib.SimpleHttp.java.AlwaysTrustingDefaultSslSocketFactory;
import com.uchump.prime.Metatron.Lib.SimpleHttp.java.AlwaysTrustingHostNameVerifier;

public enum Ssl {
    enabled {
        @Override
        public ConnectionSocketFactory getSocketFactory() {
            return SSLConnectionSocketFactory.getSocketFactory();
        }
    },
    naive {
        @Override
        public ConnectionSocketFactory getSocketFactory() {
            setPlatformSslToAlwaysTrustCertificatesAndHosts();
            return SSLConnectionSocketFactory.getSocketFactory();
        }

        private void setPlatformSslToAlwaysTrustCertificatesAndHosts() {
            new AlwaysTrustingDefaultSslSocketFactory().configureDefaultSslSocketFactory();
            new AlwaysTrustingHostNameVerifier().configureHttpsUrlConnection();
        }
    },
    disabled {
        @Override
        public ConnectionSocketFactory getSocketFactory() {
            return PlainConnectionSocketFactory.getSocketFactory();
        }
    };

    public abstract ConnectionSocketFactory getSocketFactory();
}