package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication;

import javax.servlet.http.HttpServletRequest;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.LoginService;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;

/**
 * <p>A service to query for user roles.</p>
 */
@FunctionalInterface
public interface AuthorizationService
{
    /**
     * @param request the current HTTP request
     * @param name the user name
     * @return a {@link UserIdentity} to query for roles of the given user
     */
    UserIdentity getUserIdentity(HttpServletRequest request, String name);

    /**
     * <p>Wraps a {@link LoginService} as an AuthorizationService</p>
     *
     * @param loginService the {@link LoginService} to wrap
     * @return an AuthorizationService that delegates the query for roles to the given {@link LoginService}
     */
    public static AuthorizationService from(LoginService loginService, Object credentials)
    {
        return (request, name) -> loginService.login(name, credentials, request);
    }
}