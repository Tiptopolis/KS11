package com.uchump.prime.Metatron.Lib.SimpleHttp.java;

import org.apache.http.conn.ssl.NoopHostnameVerifier;

import javax.net.ssl.HttpsURLConnection;

public class AlwaysTrustingHostNameVerifier {

	public void configureHttpsUrlConnection() {
		HttpsURLConnection.setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);
	}
}