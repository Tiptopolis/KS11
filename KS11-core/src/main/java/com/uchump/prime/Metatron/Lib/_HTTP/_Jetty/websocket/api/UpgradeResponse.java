package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The HTTP Upgrade to WebSocket Response
 */
public interface UpgradeResponse
{
    /**
     * Get the accepted WebSocket protocol.
     *
     * @return the accepted WebSocket protocol.
     */
    String getAcceptedSubProtocol();

    /**
     * Get the list of extensions that should be used for the websocket.
     *
     * @return the list of negotiated extensions to use.
     */
    List<ExtensionConfig> getExtensions();

    /**
     * Get a header value
     *
     * @param name the header name
     * @return the value (null if header doesn't exist)
     */
    String getHeader(String name);

    /**
     * Get the header names
     *
     * @return the set of header names
     */
    Set<String> getHeaderNames();

    /**
     * Get the headers map
     *
     * @return the map of headers
     */
    Map<String, List<String>> getHeaders();

    /**
     * Get the multi-value header value
     *
     * @param name the header name
     * @return the list of values (null if header doesn't exist)
     */
    List<String> getHeaders(String name);

    /**
     * Get the HTTP Response Status Code
     *
     * @return the status code
     */
    int getStatusCode();
}