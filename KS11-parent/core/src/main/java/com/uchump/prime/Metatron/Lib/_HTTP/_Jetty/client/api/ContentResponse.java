package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api;

/**
 * A specialized {@link Response} that can hold a limited content in memory.
 */
public interface ContentResponse extends Response
{
    /**
     * @return the media type of the content, such as "text/html" or "application/octet-stream"
     */
    String getMediaType();

    /**
     * @return the encoding of the content, such as "UTF-8"
     */
    String getEncoding();

    /**
     * @return the response content
     */
    byte[] getContent();

    /**
     * @return the response content as a string, decoding the bytes using the charset
     * provided by the {@code Content-Type} header, if any, or UTF-8.
     */
    String getContentAsString();
}