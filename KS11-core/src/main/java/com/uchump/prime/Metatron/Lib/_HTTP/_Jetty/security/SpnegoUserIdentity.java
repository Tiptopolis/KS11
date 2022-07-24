package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.security.Principal;
import javax.security.auth.Subject;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;

public class SpnegoUserIdentity implements UserIdentity
{
    private final Subject _subject;
    private final Principal _principal;
    private final UserIdentity _roleDelegate;

    public SpnegoUserIdentity(Subject subject, Principal principal, UserIdentity roleDelegate)
    {
        _subject = subject;
        _principal = principal;
        _roleDelegate = roleDelegate;
    }

    @Override
    public Subject getSubject()
    {
        return _subject;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return _principal;
    }

    @Override
    public boolean isUserInRole(String role, Scope scope)
    {
        return _roleDelegate != null && _roleDelegate.isUserInRole(role, scope);
    }

    public boolean isEstablished()
    {
        return _roleDelegate != null;
    }
}