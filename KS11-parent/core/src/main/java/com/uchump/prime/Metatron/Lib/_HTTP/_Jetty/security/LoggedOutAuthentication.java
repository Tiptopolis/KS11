package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import javax.servlet.ServletRequest;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.LoginAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Authentication;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;

/**
 * LoggedOutAuthentication
 *
 * An Authentication indicating that a user has been previously, but is not currently logged in,
 * but may be capable of logging in after a call to Request.login(String,String)
 */
public class LoggedOutAuthentication implements Authentication.NonAuthenticated
{
    private LoginAuthenticator _authenticator;

    public LoggedOutAuthentication(LoginAuthenticator authenticator)
    {
        _authenticator = authenticator;
    }

    @Override
    public Authentication login(String username, Object password, ServletRequest request)
    {
        if (username == null)
            return null;

        UserIdentity identity = _authenticator.login(username, password, request);
        if (identity != null)
        {
            IdentityService identityService = _authenticator.getLoginService().getIdentityService();
            UserAuthentication authentication = new UserAuthentication("API", identity);
            if (identityService != null)
                identityService.associate(identity);
            return authentication;
        }
        return null;
    }
}