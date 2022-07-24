package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;


import java.io.Serializable;
import java.util.Set;
import javax.servlet.ServletRequest;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.LoginAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Authentication;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Authentication.User;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity.Scope;


/**
 * AbstractUserAuthentication
 *
 *
 * Base class for representing an authenticated user.
 */
public abstract class AbstractUserAuthentication implements User, Serializable
{
    private static final long serialVersionUID = -6290411814232723403L;
    protected String _method;
    protected transient UserIdentity _userIdentity;

    public AbstractUserAuthentication(String method, UserIdentity userIdentity)
    {
        _method = method;
        _userIdentity = userIdentity;
    }

    @Override
    public String getAuthMethod()
    {
        return _method;
    }

    @Override
    public UserIdentity getUserIdentity()
    {
        return _userIdentity;
    }

    @Override
    public boolean isUserInRole(Scope scope, String role)
    {
        String roleToTest = null;
        if (scope != null && scope.getRoleRefMap() != null)
            roleToTest = scope.getRoleRefMap().get(role);
        if (roleToTest == null)
            roleToTest = role;
        //Servlet Spec 3.1 pg 125 if testing special role **
        if ("**".equals(roleToTest.trim()))
        {
            //if ** is NOT a declared role name, the we return true 
            //as the user is authenticated. If ** HAS been declared as a
            //role name, then we have to check if the user has that role
            if (!declaredRolesContains("**"))
                return true;
            else
                return _userIdentity.isUserInRole(role, scope);
        }

        return _userIdentity.isUserInRole(role, scope);
    }

    public boolean declaredRolesContains(String roleName)
    {
        SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
        if (security == null)
            return false;

        if (security instanceof ConstraintAware)
        {
            Set<String> declaredRoles = ((ConstraintAware)security).getRoles();
            return (declaredRoles != null) && declaredRoles.contains(roleName);
        }

        return false;
    }

    @Override
    public Authentication logout(ServletRequest request)
    {
        SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
        if (security != null)
        {
            security.logout(this);
            Authenticator authenticator = security.getAuthenticator();
            if (authenticator instanceof LoginAuthenticator)
            {
                ((LoginAuthenticator)authenticator).logout(request);
                return new LoggedOutAuthentication((LoginAuthenticator)authenticator);
            }
        }

        return Authentication.UNAUTHENTICATED;
    }
}