package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import javax.websocket.MessageHandler;

public class RegisteredMessageHandler
{
    private byte websocketMessageType;
    private Class<?> handlerType;
    private MessageHandler messageHandler;

    public RegisteredMessageHandler(byte websocketMessageType, Class<?> handlerType, MessageHandler messageHandler)
    {
        this.websocketMessageType = websocketMessageType;
        this.handlerType = handlerType;
        this.messageHandler = messageHandler;
    }

    public byte getWebsocketMessageType()
    {
        return websocketMessageType;
    }

    public Class<?> getHandlerType()
    {
        return handlerType;
    }

    public MessageHandler getMessageHandler()
    {
        return messageHandler;
    }
}