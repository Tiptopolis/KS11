package com.uchump.prime.Metatron.Lib._HTTP._Jetty.jndi;

import java.util.Iterator;
import javax.naming.Binding;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * NameEnumeration
 */
public class NameEnumeration implements NamingEnumeration<NameClassPair>
{
    Iterator<Binding> _delegate;

    public NameEnumeration(Iterator<Binding> e)
    {
        _delegate = e;
    }

    @Override
    public void close()
        throws NamingException
    {
    }

    @Override
    public boolean hasMore()
        throws NamingException
    {
        return _delegate.hasNext();
    }

    @Override
    public NameClassPair next()
        throws NamingException
    {
        Binding b = _delegate.next();
        return new NameClassPair(b.getName(), b.getClassName(), true);
    }

    @Override
    public boolean hasMoreElements()
    {
        return _delegate.hasNext();
    }

    @Override
    public NameClassPair nextElement()
    {
        Binding b = _delegate.next();
        return new NameClassPair(b.getName(), b.getClassName(), true);
    }
}