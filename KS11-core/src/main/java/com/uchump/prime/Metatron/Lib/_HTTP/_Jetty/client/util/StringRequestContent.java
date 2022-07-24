package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * <p>A {@link Request.Content} for strings.</p>
 * <p>It is possible to specify, at the constructor, an encoding used to convert
 * the string into bytes, by default UTF-8.</p>
 */
public class StringRequestContent extends BytesRequestContent
{
    public StringRequestContent(String content)
    {
        this("text/plain;charset=UTF-8", content);
    }

    public StringRequestContent(String content, Charset encoding)
    {
        this("text/plain;charset=" + encoding.name(), content, encoding);
    }

    public StringRequestContent(String contentType, String content)
    {
        this(contentType, content, StandardCharsets.UTF_8);
    }

    public StringRequestContent(String contentType, String content, Charset encoding)
    {
        super(contentType, content.getBytes(encoding));
    }
}