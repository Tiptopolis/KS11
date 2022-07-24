package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.lang.invoke.MethodHandle;
import java.util.Objects;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;

public abstract class AbstractMessageSink implements MessageSink
{
    protected final CoreSession session;
    protected final MethodHandle methodHandle;

    public AbstractMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        this.session = Objects.requireNonNull(session, "CoreSession");
        this.methodHandle = Objects.requireNonNull(methodHandle, "MethodHandle");
    }
}