package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi;

import javax.naming.NamingException;

/**
 * EnvEntry
 */
public class EnvEntry extends NamingEntry
{
    private boolean overrideWebXml;

    public EnvEntry(Object scope, String jndiName, Object objToBind, boolean overrideWebXml)
        throws NamingException
    {
        super(scope, jndiName);
        save(objToBind);
        this.overrideWebXml = overrideWebXml;
    }

    public EnvEntry(String jndiName, Object objToBind, boolean overrideWebXml)
        throws NamingException
    {
        super(jndiName);
        save(objToBind);
        this.overrideWebXml = overrideWebXml;
    }

    public EnvEntry(String jndiName, Object objToBind)
        throws NamingException
    {
        this(jndiName, objToBind, false);
    }

    public boolean isOverrideWebXml()
    {
        return this.overrideWebXml;
    }

    @Override
    protected String toStringMetaData()
    {
        return "OverrideWebXml=" + overrideWebXml;
    }
}