package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi;

import javax.naming.NamingException;

/**
 * Resource
 */
public class Resource extends NamingEntry
{
    public Resource(Object scope, String jndiName, Object objToBind)
        throws NamingException
    {
        super(scope, jndiName);
        save(objToBind);
    }

    public Resource(String jndiName, Object objToBind)
        throws NamingException
    {
        super(jndiName);
        save(objToBind);
    }
}