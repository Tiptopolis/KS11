package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;

/**
 * Interface for dealing with Incoming Frames.
 */
public interface IncomingFrames
{
    /**
     * <p>Process the incoming frame.</p>
     *
     * <p>Note: if you need to hang onto any information from the frame, be sure
     * to copy it, as the information contained in the Frame will be released
     * and/or reused by the implementation.</p>
     *
     * <p>Failure of the callback will propagate the failure back to the {@link CoreSession}
     * to fail the connection and attempt to send a close {@link Frame} if one has not been sent.</p>
     * @param frame the frame to process.
     * @param callback the read completion.
     */
    void onFrame(Frame frame, Callback callback);
}