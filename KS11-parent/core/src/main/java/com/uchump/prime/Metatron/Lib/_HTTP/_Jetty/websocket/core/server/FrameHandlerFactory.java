package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;

/**
 * Factory for FrameHandler instances
 */
public interface FrameHandlerFactory
{
    /**
     * Create a FrameHandler from the provided websocketPojo.
     *
     * @param websocketPojo the websocket pojo to work with
     * @param upgradeRequest the Upgrade Handshake Request used to create the FrameHandler
     * @param upgradeResponse the Upgrade Handshake Response used to create the FrameHandler
     * @return the API specific FrameHandler, or null if this implementation is unable to create
     * the FrameHandler (allowing another {@link FrameHandlerFactory} to try)
     */
    FrameHandler newFrameHandler(Object websocketPojo, ServerUpgradeRequest upgradeRequest, ServerUpgradeResponse upgradeResponse);
}