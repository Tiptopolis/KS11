package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.matchers;

import java.util.Map;
import javax.websocket.Decoder;
import javax.websocket.MessageHandler;
import javax.websocket.PongMessage;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.RegisteredMessageHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders.RegisteredDecoder;
import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeMatcher;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.MessageType;

public class IsMessageHandlerTypeRegistered extends TypeSafeMatcher<JavaxWebSocketSession>
{
    private final MessageType expectedType;

    public IsMessageHandlerTypeRegistered(MessageType expectedType)
    {
        this.expectedType = expectedType;
    }

    // Describe Expectation (reason or entries)
    @Override
    public void describeTo(Description description)
    {
        description.appendText(".getMessageHandlers() contains registered MessageHandler for type " + expectedType);
    }

    @Override
    protected boolean matchesSafely(JavaxWebSocketSession session)
    {
        Map<Byte, RegisteredMessageHandler> handlerMap = session.getFrameHandler().getMessageHandlerMap();

        if (handlerMap == null)
        {
            return false;
        }

        for (RegisteredMessageHandler registeredMessageHandler : handlerMap.values())
        {
            Class<?> onMessageType = registeredMessageHandler.getHandlerType();

            RegisteredDecoder registeredDecoder = session.getDecoders().getFirstRegisteredDecoder(onMessageType);
            if (registeredDecoder == null)
            {
                continue;
            }
            
            if (expectedType == MessageType.PONG)
            {
                if (PongMessage.class.isAssignableFrom(registeredDecoder.objectType))
                    return true;
                continue;
            }

            if (expectedType == MessageType.BINARY)
            {
                if (registeredDecoder.implementsInterface(Decoder.Binary.class))
                    return true;
                if (registeredDecoder.implementsInterface(Decoder.BinaryStream.class))
                    return true;
                continue;
            }

            if (expectedType == MessageType.TEXT)
            {
                if (registeredDecoder.implementsInterface(Decoder.Text.class))
                    return true;
                if (registeredDecoder.implementsInterface(Decoder.TextStream.class))
                    return true;
                continue;
            }
        }

        return false;
    }

    @Override
    protected void describeMismatchSafely(JavaxWebSocketSession session, Description mismatchDescription)
    {
        Map<Byte, RegisteredMessageHandler> handlerMap = session.getFrameHandler().getMessageHandlerMap();

        mismatchDescription.appendText(".getMessageHandlers()");

        if (handlerMap == null)
        {
            mismatchDescription.appendText(" is <null>");
            return;
        }

        mismatchDescription.appendText(" contains [");
        boolean delim = false;
        for (RegisteredMessageHandler registeredMessageHandler : handlerMap.values())
        {
            Class<? extends MessageHandler> handlerClass = registeredMessageHandler.getMessageHandler().getClass();
            if (delim)
            {
                mismatchDescription.appendText(", ");
            }
            delim = true;
            mismatchDescription.appendText(handlerClass.getName());

            Class<?> onMessageType = registeredMessageHandler.getHandlerType();

            if (onMessageType == null)
            {
                mismatchDescription.appendText("<UnknownType>");
                continue;
            }

            mismatchDescription.appendText("<" + onMessageType.getName() + ">");

            RegisteredDecoder registeredDecoder = session.getDecoders().getFirstRegisteredDecoder(onMessageType);
            if (registeredDecoder == null)
            {
                mismatchDescription.appendText("(!NO-DECODER!)");
                continue;
            }

            mismatchDescription.appendText("(" + registeredDecoder.interfaceType.getName() + ")");
        }

        mismatchDescription.appendText("]");
    }

    public static IsMessageHandlerTypeRegistered isMessageHandlerTypeRegistered(MessageType messageType)
    {
        return new IsMessageHandlerTypeRegistered(messageType);
    }
}