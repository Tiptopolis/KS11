package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.io.Serializable;
import java.security.Principal;
import javax.security.auth.Subject;

/**
 * RolePrincipal
 * 
 * Represents a role. This class can be added to a Subject to represent a role that the
 * Subject has.
 * 
 */
public class RolePrincipal implements Principal, Serializable
{
    private static final long serialVersionUID = 2998397924051854402L;
    private final String _roleName;

    public RolePrincipal(String name)
    {
        _roleName = name;
    }

    @Override
    public String getName()
    {
        return _roleName;
    }
    
    public void configureForSubject(Subject subject)
    {
        subject.getPrincipals().add(this);
    }
}