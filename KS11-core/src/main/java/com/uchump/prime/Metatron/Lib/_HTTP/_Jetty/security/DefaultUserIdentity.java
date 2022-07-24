package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.security.Principal;
import javax.security.auth.Subject;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;

/**
 * The default implementation of UserIdentity.
 */
public class DefaultUserIdentity implements UserIdentity
{
    private final Subject _subject;
    private final Principal _userPrincipal;
    private final String[] _roles;

    public DefaultUserIdentity(Subject subject, Principal userPrincipal, String[] roles)
    {
        _subject = subject;
        _userPrincipal = userPrincipal;
        _roles = roles;
    }

    @Override
    public Subject getSubject()
    {
        return _subject;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return _userPrincipal;
    }

    @Override
    public boolean isUserInRole(String role, Scope scope)
    {
        //Servlet Spec 3.1, pg 125
        if ("*".equals(role))
            return false;

        String roleToTest = null;
        if (scope != null && scope.getRoleRefMap() != null)
            roleToTest = scope.getRoleRefMap().get(role);

        //Servlet Spec 3.1, pg 125
        if (roleToTest == null)
            roleToTest = role;

        for (String r : _roles)
        {
            if (r.equals(roleToTest))
                return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return DefaultUserIdentity.class.getSimpleName() + "('" + _userPrincipal + "')";
    }
}