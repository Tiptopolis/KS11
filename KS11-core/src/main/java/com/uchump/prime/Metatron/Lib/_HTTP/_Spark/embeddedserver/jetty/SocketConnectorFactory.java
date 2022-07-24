package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty;

import java.util.concurrent.TimeUnit;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ForwardedRequestCustomizer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.HttpConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.HttpConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ssl.SslContextFactory;

import spark.ssl.SslStores;
import spark.utils.Assert;

/**
 * Creates socket connectors.
 */
public class SocketConnectorFactory {

	/**
	 * Creates an ordinary, non-secured Jetty server jetty.
	 *
	 * @param server Jetty server
	 * @param host   host
	 * @param port   port
	 * @return - a server jetty
	 */
	public static ServerConnector createSocketConnector(Server server, String host, int port,
			boolean trustForwardHeaders) {
		Assert.notNull(server, "'server' must not be null");
		Assert.notNull(host, "'host' must not be null");

		HttpConnectionFactory httpConnectionFactory = createHttpConnectionFactory(trustForwardHeaders);
		ServerConnector connector = new ServerConnector(server, httpConnectionFactory);
		initializeConnector(connector, host, port);
		return connector;
	}

	/**
	 * Creates a ssl jetty socket jetty. Keystore required, truststore optional. If
	 * truststore not specified keystore will be reused.
	 *
	 * @param server    Jetty server
	 * @param sslStores the security sslStores.
	 * @param host      host
	 * @param port      port
	 * @return a ssl socket jetty
	 */
	public static ServerConnector createSecureSocketConnector(Server server, String host, int port, SslStores sslStores,
			boolean trustForwardHeaders) {
		Assert.notNull(server, "'server' must not be null");
		Assert.notNull(host, "'host' must not be null");
		Assert.notNull(sslStores, "'sslStores' must not be null");

		SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
		sslContextFactory.setKeyStorePath(sslStores.keystoreFile());

		if (sslStores.keystorePassword() != null) {
			sslContextFactory.setKeyStorePassword(sslStores.keystorePassword());
		}

		if (sslStores.certAlias() != null) {
			sslContextFactory.setCertAlias(sslStores.certAlias());
		}

		if (sslStores.trustStoreFile() != null) {
			sslContextFactory.setTrustStorePath(sslStores.trustStoreFile());
		}

		if (sslStores.trustStorePassword() != null) {
			sslContextFactory.setTrustStorePassword(sslStores.trustStorePassword());
		}

		if (sslStores.needsClientCert()) {
			sslContextFactory.setNeedClientAuth(true);
			sslContextFactory.setWantClientAuth(true);
		}

		HttpConnectionFactory httpConnectionFactory = createHttpConnectionFactory(trustForwardHeaders);

		ServerConnector connector = new ServerConnector(server, sslContextFactory, httpConnectionFactory);
		initializeConnector(connector, host, port);
		return connector;
	}

	private static void initializeConnector(ServerConnector connector, String host, int port) {
		// Set some timeout options to make debugging easier.
		connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
		connector.setHost(host);
		connector.setPort(port);
	}

	private static HttpConnectionFactory createHttpConnectionFactory(boolean trustForwardHeaders) {
		HttpConfiguration httpConfig = new HttpConfiguration();
		httpConfig.setSecureScheme("https");
		if (trustForwardHeaders)
			httpConfig.addCustomizer(new ForwardedRequestCustomizer());
		return new HttpConnectionFactory(httpConfig);
	}

}