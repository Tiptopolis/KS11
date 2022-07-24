package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;

/**
 * Sink consumer for messages (used for multiple frames with continuations,
 * and also to allow for streaming APIs)
 */
public interface MessageSink
{
    /**
     * Consume the frame payload to the message.
     *
     * @param frame the frame, its payload (and fin state) to append
     * @param callback the callback for how the frame was consumed
     */
    void accept(Frame frame, Callback callback);
}