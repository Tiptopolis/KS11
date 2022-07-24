package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl;

import javax.net.ssl.SSLEngine;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;

public interface ALPNProcessor {
	/**
	 * Initializes this ALPNProcessor
	 *
	 * @throws RuntimeException if this processor is unavailable (e.g. missing
	 *                          dependencies or wrong JVM)
	 */
	public default void init() {
	}

	/**
	 * Tests if this processor can be applied to the given SSLEngine.
	 *
	 * @param sslEngine the SSLEngine to check
	 * @return true if the processor can be applied to the given SSLEngine
	 */
	public default boolean appliesTo(SSLEngine sslEngine) {
		return false;
	}

	/**
	 * Configures the given SSLEngine and the given Connection for ALPN.
	 *
	 * @param sslEngine  the SSLEngine to configure
	 * @param connection the Connection to configure
	 * @throws RuntimeException if this processor cannot be configured
	 */
	public default void configure(SSLEngine sslEngine, Connection connection) {
	}

	/**
	 * Server-side interface used by ServiceLoader.
	 */
	public interface Server extends ALPNProcessor {
	}

	/**
	 * Client-side interface used by ServiceLoader.
	 */
	public interface Client extends ALPNProcessor {
	}
}