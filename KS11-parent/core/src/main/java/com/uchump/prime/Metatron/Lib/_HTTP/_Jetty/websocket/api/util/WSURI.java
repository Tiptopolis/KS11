package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Utility methods for converting a {@link URI} between a HTTP(S) and WS(S) URI.
 */
public final class WSURI
{
    /**
     * Convert to HTTP {@code http} or {@code https} scheme URIs.
     * <p>
     * Converting {@code ws} and {@code wss} URIs to their HTTP equivalent
     *
     * @param inputUri the input URI
     * @return the HTTP scheme URI for the input URI.
     * @throws URISyntaxException if unable to convert the input URI
     */
    public static URI toHttp(final URI inputUri) throws URISyntaxException
    {
        Objects.requireNonNull(inputUri, "Input URI must not be null");
        String wsScheme = inputUri.getScheme();
        if ("http".equalsIgnoreCase(wsScheme) || "https".equalsIgnoreCase(wsScheme))
        {
            // leave alone
            return inputUri;
        }

        if ("ws".equalsIgnoreCase(wsScheme))
        {
            // convert to http
            return new URI("http" + inputUri.toString().substring(wsScheme.length()));
        }

        if ("wss".equalsIgnoreCase(wsScheme))
        {
            // convert to https
            return new URI("https" + inputUri.toString().substring(wsScheme.length()));
        }

        throw new URISyntaxException(inputUri.toString(), "Unrecognized WebSocket scheme");
    }

    /**
     * Convert to WebSocket {@code ws} or {@code wss} scheme URIs
     * <p>
     * Converting {@code http} and {@code https} URIs to their WebSocket equivalent
     *
     * @param inputUrl the input URI
     * @return the WebSocket scheme URI for the input URI.
     * @throws URISyntaxException if unable to convert the input URI
     */
    public static URI toWebsocket(CharSequence inputUrl) throws URISyntaxException
    {
        return toWebsocket(new URI(inputUrl.toString()));
    }

    /**
     * Convert to WebSocket {@code ws} or {@code wss} scheme URIs
     * <p>
     * Converting {@code http} and {@code https} URIs to their WebSocket equivalent
     *
     * @param inputUrl the input URI
     * @param query the optional query string
     * @return the WebSocket scheme URI for the input URI.
     * @throws URISyntaxException if unable to convert the input URI
     */
    public static URI toWebsocket(CharSequence inputUrl, String query) throws URISyntaxException
    {
        if (query == null)
        {
            return toWebsocket(new URI(inputUrl.toString()));
        }
        return toWebsocket(new URI(inputUrl.toString() + '?' + query));
    }

    /**
     * Convert to WebSocket {@code ws} or {@code wss} scheme URIs
     *
     * <p>
     * Converting {@code http} and {@code https} URIs to their WebSocket equivalent
     *
     * @param inputUri the input URI
     * @return the WebSocket scheme URI for the input URI.
     * @throws URISyntaxException if unable to convert the input URI
     */
    public static URI toWebsocket(final URI inputUri) throws URISyntaxException
    {
        Objects.requireNonNull(inputUri, "Input URI must not be null");
        String httpScheme = inputUri.getScheme();
        if (httpScheme == null)
            throw new URISyntaxException(inputUri.toString(), "Undefined HTTP scheme");

        if ("ws".equalsIgnoreCase(httpScheme) || "wss".equalsIgnoreCase(httpScheme))
            return inputUri;

        String afterScheme = inputUri.toString().substring(httpScheme.length());
        if ("http".equalsIgnoreCase(httpScheme))
            return new URI("ws" + afterScheme);
        if ("https".equalsIgnoreCase(httpScheme))
            return new URI("wss" + afterScheme);

        throw new URISyntaxException(inputUri.toString(), "Unrecognized HTTP scheme");
    }
}