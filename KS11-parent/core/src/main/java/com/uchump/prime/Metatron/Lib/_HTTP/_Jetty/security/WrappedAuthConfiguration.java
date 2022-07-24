package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.util.Set;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.Authenticator.AuthConfiguration;

/**
 * A wrapper for {@link AuthConfiguration}. This allows you create a new AuthConfiguration which can
 * override a method to change a value from an another instance of AuthConfiguration.
 */
public class WrappedAuthConfiguration implements AuthConfiguration
{
    private final AuthConfiguration _configuration;

    public WrappedAuthConfiguration(AuthConfiguration configuration)
    {
        _configuration = configuration;
    }

    @Override
    public String getAuthMethod()
    {
        return _configuration.getAuthMethod();
    }

    @Override
    public String getRealmName()
    {
        return _configuration.getRealmName();
    }

    @Override
    public String getInitParameter(String param)
    {
        return _configuration.getInitParameter(param);
    }

    @Override
    public Set<String> getInitParameterNames()
    {
        return _configuration.getInitParameterNames();
    }

    @Override
    public LoginService getLoginService()
    {
        return _configuration.getLoginService();
    }

    @Override
    public IdentityService getIdentityService()
    {
        return _configuration.getIdentityService();
    }

    @Override
    public boolean isSessionRenewedOnAuthentication()
    {
        return _configuration.isSessionRenewedOnAuthentication();
    }
}