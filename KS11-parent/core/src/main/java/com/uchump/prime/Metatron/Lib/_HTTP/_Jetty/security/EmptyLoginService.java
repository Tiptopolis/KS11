package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import javax.servlet.ServletRequest;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;

/**
 * LoginService implementation which always denies any attempt to login.
 */
public class EmptyLoginService implements LoginService
{
    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public UserIdentity login(String username, Object credentials, ServletRequest request)
    {
        return null;
    }

    @Override
    public boolean validate(UserIdentity user)
    {
        return false;
    }

    @Override
    public IdentityService getIdentityService()
    {
        return null;
    }

    @Override
    public void setIdentityService(IdentityService service)
    {
    }

    @Override
    public void logout(UserIdentity user)
    {
    }
}