package com.uchump.prime.Metatron.Lib._HTTP._Jetty.http;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.MimeTypes.Type;


public class PrecompressedHttpContent implements HttpContent
{
    private final HttpContent _content;
    private final HttpContent _precompressedContent;
    private final CompressedContentFormat _format;

    public PrecompressedHttpContent(HttpContent content, HttpContent precompressedContent, CompressedContentFormat format)
    {
        _content = content;
        _precompressedContent = precompressedContent;
        _format = format;
        if (_precompressedContent == null || _format == null)
        {
            throw new NullPointerException("Missing compressed content and/or format");
        }
    }

    @Override
    public int hashCode()
    {
        return _content.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return _content.equals(obj);
    }

    @Override
    public Resource getResource()
    {
        return _content.getResource();
    }

    @Override
    public HttpField getETag()
    {
        return new HttpField(HttpHeader.ETAG, getETagValue());
    }

    @Override
    public String getETagValue()
    {
        return _content.getResource().getWeakETag(_format.getEtagSuffix());
    }

    @Override
    public HttpField getLastModified()
    {
        return _content.getLastModified();
    }

    @Override
    public String getLastModifiedValue()
    {
        return _content.getLastModifiedValue();
    }

    @Override
    public HttpField getContentType()
    {
        return _content.getContentType();
    }

    @Override
    public String getContentTypeValue()
    {
        return _content.getContentTypeValue();
    }

    @Override
    public HttpField getContentEncoding()
    {
        return _format.getContentEncoding();
    }

    @Override
    public String getContentEncodingValue()
    {
        return _format.getContentEncoding().getValue();
    }

    @Override
    public String getCharacterEncoding()
    {
        return _content.getCharacterEncoding();
    }

    @Override
    public Type getMimeType()
    {
        return _content.getMimeType();
    }

    @Override
    public void release()
    {
        _content.release();
    }

    @Override
    public ByteBuffer getIndirectBuffer()
    {
        return _precompressedContent.getIndirectBuffer();
    }

    @Override
    public ByteBuffer getDirectBuffer()
    {
        return _precompressedContent.getDirectBuffer();
    }

    @Override
    public HttpField getContentLength()
    {
        return _precompressedContent.getContentLength();
    }

    @Override
    public long getContentLengthValue()
    {
        return _precompressedContent.getContentLengthValue();
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return _precompressedContent.getInputStream();
    }

    @Override
    public ReadableByteChannel getReadableByteChannel() throws IOException
    {
        return _precompressedContent.getReadableByteChannel();
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x{e=%s,r=%s|%s,lm=%s|%s,ct=%s}",
            this.getClass().getSimpleName(), hashCode(),
            _format,
            _content.getResource(), _precompressedContent.getResource(),
            _content.getResource().lastModified(), _precompressedContent.getResource().lastModified(),
            getContentType());
    }

    @Override
    public Map<CompressedContentFormat, HttpContent> getPrecompressedContents()
    {
        return null;
    }
}