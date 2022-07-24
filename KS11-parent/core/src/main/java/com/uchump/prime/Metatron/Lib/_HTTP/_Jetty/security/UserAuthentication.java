package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;

/**
 * @version $Rev: 4793 $ $Date: 2009-03-19 00:00:01 +0100 (Thu, 19 Mar 2009) $
 */
public class UserAuthentication extends AbstractUserAuthentication
{

    public UserAuthentication(String method, UserIdentity userIdentity)
    {
        super(method, userIdentity);
    }

    @Override
    public String toString()
    {
        return "{User," + getAuthMethod() + "," + _userIdentity + "}";
    }
}