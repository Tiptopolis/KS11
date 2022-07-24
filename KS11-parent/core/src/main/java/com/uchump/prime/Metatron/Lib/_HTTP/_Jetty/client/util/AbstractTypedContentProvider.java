package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.ContentProvider;

/**
 * @deprecated use {@link AbstractRequestContent} instead.
 */
@Deprecated
public abstract class AbstractTypedContentProvider implements ContentProvider.Typed
{
    private final String contentType;

    protected AbstractTypedContentProvider(String contentType)
    {
        this.contentType = contentType;
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }
}