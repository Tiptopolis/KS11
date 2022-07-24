package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.AbstractUserAuthentication;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.LoginService;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.SecurityHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;

/**
 * SessionAuthentication
 *
 * When a user has been successfully authenticated with some types
 * of Authenticator, the Authenticator stashes a SessionAuthentication
 * into an HttpSession to remember that the user is authenticated.
 */
public class SessionAuthentication extends AbstractUserAuthentication
    implements Serializable, HttpSessionActivationListener, HttpSessionBindingListener
{
    private static final Logger LOG = LoggerFactory.getLogger(SessionAuthentication.class);

    private static final long serialVersionUID = -4643200685888258706L;

    public static final String __J_AUTHENTICATED = "org.eclipse.jetty.security.UserIdentity";

    private final String _name;
    private final Object _credentials;
    private transient HttpSession _session;

    public SessionAuthentication(String method, UserIdentity userIdentity, Object credentials)
    {
        super(method, userIdentity);
        _name = userIdentity.getUserPrincipal().getName();
        _credentials = credentials;
    }

    @Override
    public UserIdentity getUserIdentity()
    {
        if (_userIdentity == null)
            throw new IllegalStateException("!UserIdentity");
        return super.getUserIdentity();
    }

    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException
    {
        stream.defaultReadObject();

        SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
        if (security == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("!SecurityHandler");
            return;
        }

        LoginService loginService = security.getLoginService();
        if (loginService == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("!LoginService");
            return;
        }

        _userIdentity = loginService.login(_name, _credentials, null);
        LOG.debug("Deserialized and relogged in {}", this);
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x{%s,%s}", this.getClass().getSimpleName(), hashCode(), _session == null ? "-" : _session.getId(), _userIdentity);
    }

    @Override
    public void sessionWillPassivate(HttpSessionEvent se)
    {
    }

    @Override
    public void sessionDidActivate(HttpSessionEvent se)
    {
        if (_session == null)
        {
            _session = se.getSession();
        }
    }
}