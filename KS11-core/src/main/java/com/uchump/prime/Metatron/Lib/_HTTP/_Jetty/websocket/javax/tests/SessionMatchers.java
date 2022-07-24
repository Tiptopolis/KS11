package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import javax.websocket.MessageHandler;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.matchers.IsMessageHandlerType;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.matchers.IsMessageHandlerTypeRegistered;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;

public final class SessionMatchers
{
    public static Matcher<JavaxWebSocketSession> isMessageHandlerTypeRegistered(MessageType messageType)
    {
        return IsMessageHandlerTypeRegistered.isMessageHandlerTypeRegistered(messageType);
    }

    public static Matcher<MessageHandler> isMessageHandlerType(JavaxWebSocketSession session, MessageType messageType)
    {
        return IsMessageHandlerType.isMessageHandlerType(session, messageType);
    }
}