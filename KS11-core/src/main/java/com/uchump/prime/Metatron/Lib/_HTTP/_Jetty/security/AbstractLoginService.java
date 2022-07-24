package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

import java.util.ArrayList;
import java.util.List;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.UserIdentity;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;

/**
 * AbstractLoginService
 * 
 * Base class for LoginServices that allows subclasses to provide the user authentication and authorization information,
 * but provides common behaviour such as handling authentication. 
 */
public abstract class AbstractLoginService extends ContainerLifeCycle implements LoginService
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractLoginService.class);

    protected IdentityService _identityService = new DefaultIdentityService();
    protected String _name;
    protected boolean _fullValidate = false;

    protected abstract List<RolePrincipal> loadRoleInfo(UserPrincipal user);

    protected abstract UserPrincipal loadUserInfo(String username);

    protected AbstractLoginService()
    {
        addBean(_identityService);
    }

    @Override
    public String getName()
    {
        return _name;
    }

    /**
     * Set the identityService.
     *
     * @param identityService the identityService to set
     */
    @Override
    public void setIdentityService(IdentityService identityService)
    {
        if (isRunning())
            throw new IllegalStateException("Running");
        updateBean(_identityService, identityService);
        _identityService = identityService;
    }

    /**
     * Set the name.
     *
     * @param name the name to set
     */
    public void setName(String name)
    {
        if (isRunning())
            throw new IllegalStateException("Running");
        _name = name;
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x[%s]", this.getClass().getSimpleName(), hashCode(), _name);
    }

    @Override
    public UserIdentity login(String username, Object credentials, ServletRequest request)
    {
        if (username == null)
            return null;

        UserPrincipal userPrincipal = loadUserInfo(username);
        if (userPrincipal != null && userPrincipal.authenticate(credentials))
        {
            //safe to load the roles
            List<RolePrincipal> roles = loadRoleInfo(userPrincipal);

            List<String> roleNames = new ArrayList<>();
            Subject subject = new Subject();
            userPrincipal.configureSubject(subject);
            if (roles != null)
            {
                roles.forEach(p -> 
                {
                    p.configureForSubject(subject);
                    roleNames.add(p.getName());
                });
            }
  
            subject.setReadOnly();
            return _identityService.newUserIdentity(subject, userPrincipal, roleNames.toArray(new String[0]));
        }

        return null;
    }

    @Override
    public boolean validate(UserIdentity user)
    {
        if (!isFullValidate())
            return true; //if we have a user identity it must be valid

        //Do a full validation back against the user store     
        UserPrincipal fresh = loadUserInfo(user.getUserPrincipal().getName());
        if (fresh == null)
            return false; //user no longer exists

        if (user.getUserPrincipal() instanceof UserPrincipal)
        {
            return fresh.authenticate(((UserPrincipal)user.getUserPrincipal()));
        }

        throw new IllegalStateException("UserPrincipal not known"); //can't validate
    }

    @Override
    public IdentityService getIdentityService()
    {
        return _identityService;
    }

    @Override
    public void logout(UserIdentity user)
    {
        //Override in subclasses
    }

    public boolean isFullValidate()
    {
        return _fullValidate;
    }

    public void setFullValidate(boolean fullValidate)
    {
        _fullValidate = fullValidate;
    }
}