package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication;

import java.security.Principal;
import javax.security.auth.Subject;

/**
 * This is similar to the jaspi PasswordValidationCallback but includes user
 * principal and group info as well.
 *
 * @version $Rev: 4792 $ $Date: 2009-03-18 22:55:52 +0100 (Wed, 18 Mar 2009) $
 */
public interface LoginCallback
{
    public Subject getSubject();

    public String getUserName();

    public Object getCredential();

    public boolean isSuccess();

    public void setSuccess(boolean success);

    public Principal getUserPrincipal();

    public void setUserPrincipal(Principal userPrincipal);

    public String[] getRoles();

    public void setRoles(String[] roles);

    public void clearPassword();
}