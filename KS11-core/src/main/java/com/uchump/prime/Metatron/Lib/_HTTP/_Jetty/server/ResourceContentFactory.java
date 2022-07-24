package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;


import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.CompressedContentFormat;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpContent;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpContent.ContentFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.MimeTypes;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.ResourceHttpContent;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.ResourceFactory;

/**
 * An HttpContent.Factory for transient content (not cached).  The HttpContent's created by
 * this factory are not intended to be cached, so memory limits for individual
 * HttpOutput streams are enforced.
 */
public class ResourceContentFactory implements ContentFactory
{
    private final ResourceFactory _factory;
    private final MimeTypes _mimeTypes;
    private final CompressedContentFormat[] _precompressedFormats;

    public ResourceContentFactory(ResourceFactory factory, MimeTypes mimeTypes, CompressedContentFormat[] precompressedFormats)
    {
        _factory = factory;
        _mimeTypes = mimeTypes;
        _precompressedFormats = precompressedFormats;
    }

    @Override
    public HttpContent getContent(String pathInContext, int maxBufferSize) throws IOException
    {
        try
        {
            // try loading the content from our factory.
            Resource resource = _factory.getResource(pathInContext);
            return load(pathInContext, resource, maxBufferSize);
        }
        catch (Throwable t)
        {
            // There are many potential Exceptions that can reveal a fully qualified path.
            // See Issue #2560 - Always wrap a Throwable here in an InvalidPathException
            // that is limited to only the provided pathInContext.
            // The cause (which might reveal a fully qualified path) is still available,
            // on the Exception and the logging, but is not reported in normal error page situations.
            // This specific exception also allows WebApps to specifically hook into a known / reliable
            // Exception type for ErrorPageErrorHandling logic.
            InvalidPathException saferException = new InvalidPathException(pathInContext, "Invalid PathInContext");
            saferException.initCause(t);
            throw saferException;
        }
    }

    private HttpContent load(String pathInContext, Resource resource, int maxBufferSize)
        throws IOException
    {
        if (resource == null || !resource.exists())
            return null;

        if (resource.isDirectory())
            return new ResourceHttpContent(resource, _mimeTypes.getMimeByExtension(resource.toString()), maxBufferSize);

        // Look for a precompressed resource or content
        String mt = _mimeTypes.getMimeByExtension(pathInContext);
        if (_precompressedFormats.length > 0)
        {
            // Is there a compressed resource?
            Map<CompressedContentFormat, HttpContent> compressedContents = new HashMap<>(_precompressedFormats.length);
            for (CompressedContentFormat format : _precompressedFormats)
            {
                String compressedPathInContext = pathInContext + format.getExtension();
                Resource compressedResource = _factory.getResource(compressedPathInContext);
                if (compressedResource != null && compressedResource.exists() && compressedResource.lastModified() >= resource.lastModified() &&
                    compressedResource.length() < resource.length())
                    compressedContents.put(format,
                        new ResourceHttpContent(compressedResource, _mimeTypes.getMimeByExtension(compressedPathInContext), maxBufferSize));
            }
            if (!compressedContents.isEmpty())
                return new ResourceHttpContent(resource, mt, maxBufferSize, compressedContents);
        }
        return new ResourceHttpContent(resource, mt, maxBufferSize);
    }

    @Override
    public String toString()
    {
        return "ResourceContentFactory[" + _factory + "]@" + hashCode();
    }
}