package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;


import java.io.IOException;
import java.net.URLClassLoader;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Dumpable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.DumpableCollection;

public class ClassLoaderDump implements Dumpable
{

    final ClassLoader _loader;

    public ClassLoaderDump(ClassLoader loader)
    {
        _loader = loader;
    }

    @Override
    public String dump()
    {
        return Dumpable.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException
    {
        if (_loader == null)
            out.append("No ClassLoader\n");
        else if (_loader instanceof Dumpable)
        {
            ((Dumpable)_loader).dump(out, indent);
        }
        else if (_loader instanceof URLClassLoader)
        {
            String loader = _loader.toString();
            DumpableCollection urls = DumpableCollection.fromArray("URLs", ((URLClassLoader)_loader).getURLs());
            ClassLoader parent = _loader.getParent();
            if (parent == null)
                Dumpable.dumpObjects(out, indent, loader, urls);
            else if (parent == Server.class.getClassLoader())
                Dumpable.dumpObjects(out, indent, loader, urls, parent.toString());
            else if (parent instanceof Dumpable)
                Dumpable.dumpObjects(out, indent, loader, urls, parent);
            else
                Dumpable.dumpObjects(out, indent, loader, urls, new ClassLoaderDump(parent));
        }
        else if (_loader.getDefinedPackages() != null)
        {
            DumpableCollection packages = DumpableCollection.from("packages", (Object[])_loader.getDefinedPackages());
            ClassLoader parent = _loader.getParent();
            if (parent == Server.class.getClassLoader())
                Dumpable.dumpObjects(out, indent, _loader, packages, "Server loader: " + parent);
            else if (parent instanceof Dumpable)
                Dumpable.dumpObjects(out, indent, _loader, packages, parent);
            else if (parent != null)
                Dumpable.dumpObjects(out, indent, _loader, packages, new ClassLoaderDump(parent));
            else
                Dumpable.dumpObjects(out, indent, _loader, packages);
        }
        else
        {
            String loader = _loader.toString();
            ClassLoader parent = _loader.getParent();
            if (parent == null)
                Dumpable.dumpObject(out, loader);
            if (parent == Server.class.getClassLoader())
                Dumpable.dumpObjects(out, indent, loader, parent.toString());
            else if (parent instanceof Dumpable)
                Dumpable.dumpObjects(out, indent, loader, parent);
            else if (parent != null)
                Dumpable.dumpObjects(out, indent, loader, new ClassLoaderDump(parent));
        }
    }
}