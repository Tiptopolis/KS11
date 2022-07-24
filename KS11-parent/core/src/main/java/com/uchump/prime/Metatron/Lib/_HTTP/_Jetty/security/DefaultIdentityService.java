package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.security.Principal;
import javax.security.auth.Subject;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;


/**
 * Default Identity Service implementation.
 * This service handles only role reference maps passed in an
 * associated {@link org.eclipse.jetty.server.UserIdentity.Scope}.  If there are roles
 * refs present, then associate will wrap the UserIdentity with one
 * that uses the role references in the
 * {@link org.eclipse.jetty.server.UserIdentity#isUserInRole(String, org.eclipse.jetty.server.UserIdentity.Scope)}
 * implementation. All other operations are effectively noops.
 */
public class DefaultIdentityService implements IdentityService
{

    public DefaultIdentityService()
    {
    }

    /**
     * If there are roles refs present in the scope, then wrap the UserIdentity
     * with one that uses the role references in the {@link UserIdentity#isUserInRole(String, org.eclipse.jetty.server.UserIdentity.Scope)}
     */
    @Override
    public Object associate(UserIdentity user)
    {
        return null;
    }

    @Override
    public void disassociate(Object previous)
    {
    }

    @Override
    public Object setRunAs(UserIdentity user, RunAsToken token)
    {
        return token;
    }

    @Override
    public void unsetRunAs(Object lastToken)
    {
    }

    @Override
    public RunAsToken newRunAsToken(String runAsName)
    {
        return new RoleRunAsToken(runAsName);
    }

    @Override
    public UserIdentity getSystemUserIdentity()
    {
        return null;
    }

    @Override
    public UserIdentity newUserIdentity(final Subject subject, final Principal userPrincipal, final String[] roles)
    {
        return new DefaultUserIdentity(subject, userPrincipal, roles);
    }
}