package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

/**
 * The possible batch modes when enqueuing outgoing frames.
 */
public enum BatchMode
{
    /**
     * Implementers are free to decide whether to send or not frames
     * to the network layer.
     */
    AUTO,

    /**
     * Implementers must batch frames.
     */
    ON,

    /**
     * Implementers must send frames to the network layer.
     */
    OFF;

    public static BatchMode max(BatchMode one, BatchMode two)
    {
        // Return the BatchMode that has the higher priority, where AUTO < ON < OFF.
        return one.ordinal() < two.ordinal() ? two : one;
    }
}