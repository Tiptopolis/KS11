package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.annotation;

import java.util.Objects;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletHolder;


/**
 * RunAs
 * <p>
 * Represents a <code>&lt;run-as&gt;</code> element in web.xml, or a <code>&#064;RunAs</code> annotation.
 * @deprecated unused as of 9.4.28 due for removal in 10.0.0
 */
@Deprecated
public class RunAs
{
    private String _className;
    private String _roleName;

    public RunAs(String className, String roleName)
    {
        _className = Objects.requireNonNull(className);
        _roleName = Objects.requireNonNull(roleName);
    }

    public String getTargetClassName()
    {
        return _className;
    }

    public String getRoleName()
    {
        return _roleName;
    }

    public void setRunAs(ServletHolder holder)
    {
        if (holder == null)
            return;
        String className = holder.getClassName();

        if (className.equals(_className))
        {
            //Only set the RunAs if it has not already been set, presumably by web/web-fragment.xml
            if (holder.getRegistration().getRunAsRole() == null)
                holder.getRegistration().setRunAsRole(_roleName);
        }
    }
}