package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;
/**
 * @version $Rev: 4701 $ $Date: 2009-03-03 13:01:26 +0100 (Tue, 03 Mar 2009) $
 */
public class RoleRunAsToken implements RunAsToken
{
    private final String _runAsRole;

    public RoleRunAsToken(String runAsRole)
    {
        this._runAsRole = runAsRole;
    }

    public String getRunAsRole()
    {
        return _runAsRole;
    }

    @Override
    public String toString()
    {
        return "RoleRunAsToken(" + _runAsRole + ")";
    }
}