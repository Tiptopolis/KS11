package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

/**
 * Connection suspend token
 */
public interface SuspendToken
{
    /**
     * Resume a previously suspended connection.
     */
    void resume();
}