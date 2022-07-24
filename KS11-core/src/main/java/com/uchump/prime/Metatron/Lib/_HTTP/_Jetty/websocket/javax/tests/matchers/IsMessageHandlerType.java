package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.matchers;

import javax.websocket.Decoder;
import javax.websocket.MessageHandler;
import javax.websocket.PongMessage;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.ReflectUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.JavaxWebSocketSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders.RegisteredDecoder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.MessageType;
import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeMatcher;


public class IsMessageHandlerType extends TypeSafeMatcher<MessageHandler>
{
    private final JavaxWebSocketSession session;
    private final MessageType expectedType;

    public IsMessageHandlerType(JavaxWebSocketSession session, MessageType expectedType)
    {
        this.session = session;
        this.expectedType = expectedType;
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText("supports a ");
        switch (expectedType)
        {
            case BINARY:
            case TEXT:
                description.appendText(expectedType.name()).appendText(" based argument/Decoder");
                break;
            case PONG:
                description.appendText(PongMessage.class.getName()).appendText(" argument");
                break;
            default:
                throw new IllegalStateException(expectedType.toString());
        }
    }

    @Override
    protected boolean matchesSafely(MessageHandler messageHandler)
    {
        Class<? extends MessageHandler> handlerClass = messageHandler.getClass();
        Class<?> onMessageClass = null;

        if (MessageHandler.Whole.class.isAssignableFrom(handlerClass))
        {
            onMessageClass = ReflectUtils.findGenericClassFor(handlerClass, MessageHandler.Whole.class);
        }
        else if (MessageHandler.Partial.class.isAssignableFrom(handlerClass))
        {
            onMessageClass = ReflectUtils.findGenericClassFor(handlerClass, MessageHandler.Partial.class);
        }

        if (onMessageClass == null)
        {
            return false;
        }

        RegisteredDecoder registeredDecoder = session.getDecoders().getFirstRegisteredDecoder(onMessageClass);
        if (registeredDecoder == null)
        {
            return false;
        }

        switch (expectedType)
        {
            case PONG:
                return PongMessage.class.isAssignableFrom(registeredDecoder.objectType);
            case BINARY:
                return (Decoder.Binary.class.isAssignableFrom(registeredDecoder.interfaceType) ||
                    Decoder.BinaryStream.class.isAssignableFrom(registeredDecoder.interfaceType));
            case TEXT:
                return (Decoder.Text.class.isAssignableFrom(registeredDecoder.interfaceType) ||
                    Decoder.TextStream.class.isAssignableFrom(registeredDecoder.interfaceType));
            default:
                return false;
        }
    }

    public static IsMessageHandlerType isMessageHandlerType(JavaxWebSocketSession session, MessageType messageType)
    {
        return new IsMessageHandlerType(session, messageType);
    }
}