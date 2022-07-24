package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.framehandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;

public class FrameEcho implements FrameHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(FrameEcho.class);

    private CoreSession coreSession;

    @Override
    public void onOpen(CoreSession coreSession, Callback callback)
    {
        this.coreSession = coreSession;
        callback.succeeded();
    }

    @Override
    public void onFrame(Frame frame, Callback callback)
    {
        if (frame.isControlFrame())
            callback.succeeded();
        else
            coreSession.sendFrame(Frame.copy(frame), callback, false);
    }

    @Override
    public void onError(Throwable cause, Callback callback)
    {
        if (LOG.isDebugEnabled())
            LOG.debug(this + " onError ", cause);
        callback.succeeded();
    }

    @Override
    public void onClosed(CloseStatus closeStatus, Callback callback)
    {
        coreSession = null;
        callback.succeeded();
    }
}