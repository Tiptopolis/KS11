package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.AbstractLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.security.Credential;

/**
 * Store of user authentication and authorization information.
 * 
 */
public class UserStore extends AbstractLifeCycle
{
    protected final Map<String, User> _users = new ConcurrentHashMap<>();
    
    protected class User
    {
        protected UserPrincipal _userPrincipal;
        protected List<RolePrincipal> _rolePrincipals = Collections.emptyList();
        
        protected User(String username, Credential credential, String[] roles)
        {
            _userPrincipal = new UserPrincipal(username, credential);

            _rolePrincipals = Collections.emptyList();
            
            if (roles != null)
                _rolePrincipals = Arrays.stream(roles).map(RolePrincipal::new).collect(Collectors.toList());
        }
        
        protected UserPrincipal getUserPrincipal()
        {
            return _userPrincipal;
        }
        
        protected List<RolePrincipal> getRolePrincipals()
        {
            return _rolePrincipals;
        }
    }
    
    public void addUser(String username, Credential credential, String[] roles)
    {
        _users.put(username, new User(username, credential, roles));
    }

    public void removeUser(String username)
    {
        _users.remove(username);
    }
    
    public UserPrincipal getUserPrincipal(String username)
    {
        User user = _users.get(username);
        return (user == null ? null : user.getUserPrincipal());
    }
    
    public List<RolePrincipal> getRolePrincipals(String username)
    {
        User user = _users.get(username);
        return (user == null ? null : user.getRolePrincipals());
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x[users.count=%d]", getClass().getSimpleName(), hashCode(), _users.size());
    }
}