package com.uchump.prime.Metatron.Lib._HTTP._Jetty.jndi;

import java.util.Iterator;
import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * BindingEnumeration
 */
public class BindingEnumeration implements NamingEnumeration<Binding>
{
    Iterator<Binding> _delegate;

    public BindingEnumeration(Iterator<Binding> e)
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
    public Binding next()
        throws NamingException
    {
        Binding b = (Binding)_delegate.next();
        return new Binding(b.getName(), b.getClassName(), b.getObject(), true);
    }

    @Override
    public boolean hasMoreElements()
    {
        return _delegate.hasNext();
    }

    @Override
    public Binding nextElement()
    {
        Binding b = (Binding)_delegate.next();
        return new Binding(b.getName(), b.getClassName(), b.getObject(), true);
    }
}