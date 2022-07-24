package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;

public class DemandingIncomingFramesCapture extends IncomingFramesCapture
{
    private final WebSocketCoreSession _coreSession;

    public DemandingIncomingFramesCapture(WebSocketCoreSession coreSession)
    {
        _coreSession = coreSession;
    }

    @Override
    public void onFrame(Frame frame, Callback callback)
    {
        try
        {
            super.onFrame(frame, callback);
        }
        finally
        {
            if (!_coreSession.isDemanding())
                _coreSession.autoDemand();
        }
    }
}