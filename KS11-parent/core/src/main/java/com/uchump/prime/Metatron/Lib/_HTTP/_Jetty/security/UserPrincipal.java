package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.io.Serializable;
import java.security.Principal;
import javax.security.auth.Subject;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.security.Credential;

/**
 * UserPrincipal
 * 
 * Represents a user with a credential.
 * Instances of this class can be added to a Subject to
 * present the user, while the credentials can be added 
 * directly to the Subject.
 */
public class UserPrincipal implements Principal, Serializable
{
    private static final long serialVersionUID = -6226920753748399662L;
    private final String _name;
    protected final Credential _credential;

    public UserPrincipal(String name, Credential credential)
    {
        _name = name;
        _credential = credential;
    }

    public boolean authenticate(Object credentials)
    {
        return _credential != null && _credential.check(credentials);
    }

    public boolean authenticate(Credential c)
    {
        return (_credential != null && c != null && _credential.equals(c));
    }
    
    public boolean authenticate(UserPrincipal u)
    {
        return (u != null && authenticate(u._credential));
    }

    public void configureSubject(Subject subject)
    {
        if (subject == null)
            return;

        subject.getPrincipals().add(this);
        if (_credential != null)
            subject.getPrivateCredentials().add(_credential); 
    }

    public void deconfigureSubject(Subject subject)
    {
        if (subject == null)
            return;
        subject.getPrincipals().remove(this);
        if (_credential != null)
            subject.getPrivateCredentials().remove(_credential);
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public String toString()
    {
        return _name;
    }
}