package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.Authenticator.AuthConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.BasicAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.ClientCertAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.ConfigurableSpnegoAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.DigestAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.FormAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.SslClientCertAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.security.Constraint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ssl.SslContextFactory;

/**
 * The Default Authenticator Factory. Uses the
 * {@link AuthConfiguration#getAuthMethod()} to select an {@link Authenticator}
 * from:
 * <ul>
 * <li>{@link org.eclipse.jetty.security.authentication.BasicAuthenticator}</li>
 * <li>{@link org.eclipse.jetty.security.authentication.DigestAuthenticator}</li>
 * <li>{@link org.eclipse.jetty.security.authentication.FormAuthenticator}</li>
 * <li>{@link org.eclipse.jetty.security.authentication.ClientCertAuthenticator}</li>
 * <li>{@link org.eclipse.jetty.security.authentication.SslClientCertAuthenticator}</li>
 * </ul>
 * All authenticators derived from
 * {@link org.eclipse.jetty.security.authentication.LoginAuthenticator} are
 * wrapped with a
 * {@link org.eclipse.jetty.security.authentication.DeferredAuthentication}
 * instance, which is used if authentication is not mandatory.
 *
 * The Authentications from the
 * {@link org.eclipse.jetty.security.authentication.FormAuthenticator} are
 * always wrapped in a
 * {@link org.eclipse.jetty.security.authentication.SessionAuthentication}
 * <p>
 * If a {@link LoginService} has not been set on this factory, then the service
 * is selected by searching the {@link Server#getBeans(Class)} results for a
 * service that matches the realm name, else the first LoginService found is
 * used.
 */
public class DefaultAuthenticatorFactory implements Authenticator.Factory {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticatorFactory.class);

	LoginService _loginService;

	@Override
	public Authenticator getAuthenticator(Server server, ServletContext context, AuthConfiguration configuration,
			IdentityService identityService, LoginService loginService) {
		String auth = configuration.getAuthMethod();
		Authenticator authenticator = null;

		if (Constraint.__BASIC_AUTH.equalsIgnoreCase(auth))
			authenticator = new BasicAuthenticator();
		else if (Constraint.__DIGEST_AUTH.equalsIgnoreCase(auth))
			authenticator = new DigestAuthenticator();
		else if (Constraint.__FORM_AUTH.equalsIgnoreCase(auth))
			authenticator = new FormAuthenticator();
		else if (Constraint.__SPNEGO_AUTH.equalsIgnoreCase(auth))
			authenticator = new ConfigurableSpnegoAuthenticator();
		else if (Constraint.__NEGOTIATE_AUTH.equalsIgnoreCase(auth)) // see Bug #377076
			authenticator = new ConfigurableSpnegoAuthenticator(Constraint.__NEGOTIATE_AUTH);
		if (Constraint.__CERT_AUTH.equalsIgnoreCase(auth) || Constraint.__CERT_AUTH2.equalsIgnoreCase(auth)) {
			Collection<SslContextFactory> sslContextFactories = server.getBeans(SslContextFactory.class);
			if (sslContextFactories.size() != 1) {
				if (sslContextFactories.size() > 1) {
					LOG.info(
							"Multiple SslContextFactory instances discovered. Directly configure a SslClientCertAuthenticator to use one.");
				} else {
					LOG.debug(
							"No SslContextFactory instances discovered. Directly configure a SslClientCertAuthenticator to use one.");
				}
				authenticator = new ClientCertAuthenticator();
			} else {
				authenticator = new SslClientCertAuthenticator(sslContextFactories.iterator().next());
			}
		}

		return authenticator;
	}

	/**
	 * @return the loginService
	 */
	public LoginService getLoginService() {
		return _loginService;
	}

	/**
	 * @param loginService the loginService to set
	 */
	public void setLoginService(LoginService loginService) {
		_loginService = loginService;
	}
}