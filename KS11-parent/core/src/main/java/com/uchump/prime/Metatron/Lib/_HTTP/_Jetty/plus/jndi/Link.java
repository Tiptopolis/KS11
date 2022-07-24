package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi;

import javax.naming.NamingException;

public class Link extends NamingEntry
{
    private final String _link;

    public Link(Object scope, String jndiName, String link) throws NamingException
    {
        //jndiName is the name according to the web.xml
        //objectToBind is the name in the environment
        super(scope, jndiName);
        save(link);
        _link = link;
    }

    public Link(String jndiName, String link) throws NamingException
    {
        super(jndiName);
        save(link);
        _link = link;
    }

    @Override
    public void bindToENC(String localName) throws NamingException
    {
        throw new UnsupportedOperationException("Method not supported for Link objects");
    }

    public String getLink()
    {
        return _link;
    }

    @Override
    protected String toStringMetaData()
    {
        return _link;
    }
}