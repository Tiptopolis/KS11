package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpScheme;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ClientConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.HostPort;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ssl.SslContextFactory;

/**
 * The configuration of the forward proxy to use with
 * {@link org.eclipse.jetty.client.HttpClient}.
 * <p>
 * Applications add subclasses of {@link Proxy} to this configuration via:
 * 
 * <pre>
 * ProxyConfiguration proxyConfig = httpClient.getProxyConfiguration();
 * proxyConfig.getProxies().add(new HttpProxy(proxyHost, 8080));
 * </pre>
 *
 * @see HttpClient#getProxyConfiguration()
 */
public class ProxyConfiguration {
	private final List<Proxy> proxies = new ArrayList<>();

	public List<Proxy> getProxies() {
		return proxies;
	}

	public Proxy match(Origin origin) {
		for (Proxy proxy : getProxies()) {
			if (proxy.matches(origin))
				return proxy;
		}
		return null;
	}

	public abstract static class Proxy {
		// TODO use InetAddressSet? Or IncludeExcludeSet?
		private final Set<String> included = new HashSet<>();
		private final Set<String> excluded = new HashSet<>();
		private final Origin origin;
		private final SslContextFactory.Client sslContextFactory;

		protected Proxy(Origin.Address address, boolean secure, SslContextFactory.Client sslContextFactory,
				Origin.Protocol protocol) {
			this(new Origin(secure ? HttpScheme.HTTPS.asString() : HttpScheme.HTTP.asString(), address, null, protocol),
					sslContextFactory);
		}

		protected Proxy(Origin origin, SslContextFactory.Client sslContextFactory) {
			this.origin = origin;
			this.sslContextFactory = sslContextFactory;
		}

		public Origin getOrigin() {
			return origin;
		}

		/**
		 * @return the address of this proxy
		 */
		public Origin.Address getAddress() {
			return origin.getAddress();
		}

		/**
		 * @return whether the connection to the proxy must be secured via TLS
		 */
		public boolean isSecure() {
			return HttpScheme.HTTPS.is(origin.getScheme());
		}

		/**
		 * @return the optional SslContextFactory to use when connecting to proxies
		 */
		public SslContextFactory.Client getSslContextFactory() {
			return sslContextFactory;
		}

		/**
		 * @return the protocol spoken by this proxy
		 */
		public Origin.Protocol getProtocol() {
			return origin.getProtocol();
		}

		/**
		 * @return the list of origins that must be proxied
		 * @see #matches(Origin)
		 * @see #getExcludedAddresses()
		 */
		public Set<String> getIncludedAddresses() {
			return included;
		}

		/**
		 * @return the list of origins that must not be proxied.
		 * @see #matches(Origin)
		 * @see #getIncludedAddresses()
		 */
		public Set<String> getExcludedAddresses() {
			return excluded;
		}

		/**
		 * @return an URI representing this proxy, or null if no URI can represent this
		 *         proxy
		 */
		public URI getURI() {
			return null;
		}

		/**
		 * Matches the given {@code origin} with the included and excluded addresses,
		 * returning true if the given {@code origin} is to be proxied.
		 *
		 * @param origin the origin to test for proxying
		 * @return true if the origin must be proxied, false otherwise
		 */
		public boolean matches(Origin origin) {
			if (getAddress().equals(origin.getAddress()))
				return false;

			boolean result = included.isEmpty();
			Origin.Address address = origin.getAddress();
			for (String included : this.included) {
				if (matches(address, included)) {
					result = true;
					break;
				}
			}
			for (String excluded : this.excluded) {
				if (matches(address, excluded)) {
					result = false;
					break;
				}
			}
			return result;
		}

		private boolean matches(Origin.Address address, String pattern) {
			// TODO: add support for CIDR notation like 192.168.0.0/24, see DoSFilter
			HostPort hostPort = new HostPort(pattern);
			String host = hostPort.getHost();
			int port = hostPort.getPort();
			return host.equals(address.getHost()) && (port <= 0 || port == address.getPort());
		}

		/**
		 * @param connectionFactory the nested {@link ClientConnectionFactory}
		 * @return a new {@link ClientConnectionFactory} for this Proxy
		 */
		public abstract ClientConnectionFactory newClientConnectionFactory(ClientConnectionFactory connectionFactory);

		@Override
		public String toString() {
			return origin.toString();
		}
	}
}