package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi;

import javax.naming.InitialContext;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.StringUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Dumpable;


/**
 * A utility Dumpable to dump a JNDI naming context tree.
 */
public class NamingDump implements Dumpable
{
    private final ClassLoader _loader;
    private final String _name;

    public NamingDump()
    {
        this(null, "");
    }

    public NamingDump(ClassLoader loader, String name)
    {
        _loader = loader;
        _name = name;
    }

    @Override
    public void dump(Appendable out, String indent)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try
        {
            if (!StringUtil.isBlank(_name))
                out.append(_name).append(" ");
            if (_loader != null)
                Thread.currentThread().setContextClassLoader(_loader);
            Object context = new InitialContext().lookup(_name);
            if (context instanceof Dumpable)
                ((Dumpable)context).dump(out, indent);
            else
                Dumpable.dumpObjects(out, indent, context);
        }
        catch (Throwable th)
        {
            throw new RuntimeException(th);
        }
        finally
        {
            if (_loader != null)
                Thread.currentThread().setContextClassLoader(loader);
        }
    }
}