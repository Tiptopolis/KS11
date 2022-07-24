package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;

public class EchoFrameHandler extends TestAsyncFrameHandler
{
    private boolean throwOnFrame;

    public void throwOnFrame()
    {
        throwOnFrame = true;
    }

    public EchoFrameHandler()
    {
        super(EchoFrameHandler.class.getName());
    }

    public EchoFrameHandler(String name)
    {
        super(name);
    }

    @Override
    public void onFrame(Frame frame, Callback callback)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("[{}] onFrame {}", name, frame);
        receivedFrames.offer(Frame.copy(frame));

        if (throwOnFrame)
            throw new RuntimeException("intentionally throwing in server onFrame()");

        if (frame.isDataFrame())
        {
            if (LOG.isDebugEnabled())
                LOG.debug("[{}] echoDataFrame {}", name, frame);
            Frame echo = new Frame(frame.getOpCode(), frame.isFin(), frame.getPayload());
            coreSession.sendFrame(echo, callback, false);
        }
        else
        {
            callback.succeeded();
        }
    }
}