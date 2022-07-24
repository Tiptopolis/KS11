package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IO;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;

public class FileDestroyable implements Destroyable
{
    private static final Logger LOG = LoggerFactory.getLogger(FileDestroyable.class);
    final List<File> _files = new ArrayList<File>();

    public FileDestroyable()
    {
    }

    public FileDestroyable(String file) throws IOException
    {
        _files.add(Resource.newResource(file).getFile());
    }

    public FileDestroyable(File file)
    {
        _files.add(file);
    }

    public void addFile(String file) throws IOException
    {
        try (Resource r = Resource.newResource(file);)
        {
            _files.add(r.getFile());
        }
    }

    public void addFile(File file)
    {
        _files.add(file);
    }

    public void addFiles(Collection<File> files)
    {
        _files.addAll(files);
    }

    public void removeFile(String file) throws IOException
    {
        try (Resource r = Resource.newResource(file);)
        {
            _files.remove(r.getFile());
        }
    }

    public void removeFile(File file)
    {
        _files.remove(file);
    }

    @Override
    public void destroy()
    {
        for (File file : _files)
        {
            if (file.exists())
            {
                if (LOG.isDebugEnabled())
                    LOG.debug("Destroy {}", file);
                IO.delete(file);
            }
        }
    }
}