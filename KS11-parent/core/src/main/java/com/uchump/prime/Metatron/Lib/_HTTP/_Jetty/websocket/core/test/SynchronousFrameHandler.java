package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;

public interface SynchronousFrameHandler extends FrameHandler
{
    @Override
    default void onOpen(CoreSession coreSession, Callback callback)
    {
        try
        {
            onOpen(coreSession);
            callback.succeeded();
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
    }

    default void onOpen(CoreSession coreSession) throws Exception
    {
    }

    @Override
    default void onFrame(Frame frame, Callback callback)
    {
        try
        {
            onFrame(frame);
            callback.succeeded();
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
    }

    default void onFrame(Frame frame) throws Exception
    {
    }

    @Override
    default void onClosed(CloseStatus closeStatus, Callback callback)
    {
        try
        {
            onClosed(closeStatus);
            callback.succeeded();
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
    }

    default void onClosed(CloseStatus closeStatus) throws Exception
    {
    }

    @Override
    default void onError(Throwable cause, Callback callback)
    {
        try
        {
            onError(cause);
            callback.succeeded();
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
    }

    default void onError(Throwable cause) throws Exception
    {
    }
}