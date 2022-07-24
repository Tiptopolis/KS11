package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.net.URI;
import java.security.Principal;

public interface UpgradeRequest
{
    /**
     * For {@link javax.websocket.Session#getUserPrincipal()}
     *
     * @return the User {@link Principal} present during the Upgrade Request
     */
    Principal getUserPrincipal();

    /**
     * @return the full URI of this request.
     */
    URI getRequestURI();

    /**
     * For obtaining {@link javax.websocket.server.PathParam} values from the Request context path.
     *
     * @return the path in Context.
     */
    String getPathInContext();
}