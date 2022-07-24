package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.framehandlers;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.MessageHandler;

public class StaticText extends MessageHandler
{
    private final String staticMessage;

    public StaticText(String message)
    {
        this.staticMessage = message;
    }

    @Override
    public void onText(String wholeMessage, Callback callback)
    {
        sendText(staticMessage, callback, false);
    }
}