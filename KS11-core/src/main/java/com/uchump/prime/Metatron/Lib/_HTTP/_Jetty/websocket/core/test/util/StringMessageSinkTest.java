package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BlockingArrayQueue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.FutureCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Utf8Appendable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.MessageTooLargeException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.StringMessageSink;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringMessageSinkTest
{
    private CoreSession coreSession = new CoreSession.Empty();
    private OnMessageEndpoint endpoint = new OnMessageEndpoint();

    @Test
    public void testMaxMessageSize() throws Exception
    {  
        StringMessageSink messageSink = new StringMessageSink(coreSession, endpoint.getMethodHandle());
        ByteBuffer utf8Payload = BufferUtil.toBuffer(new byte[]{(byte)0xF0, (byte)0x90, (byte)0x8D, (byte)0x88});

        FutureCallback callback = new FutureCallback();
        coreSession.setMaxTextMessageSize(3);
        messageSink.accept(new Frame(OpCode.TEXT, utf8Payload).setFin(true), callback);

        // Callback should fail and we don't receive the message in the sink.
        RuntimeException error = assertThrows(RuntimeException.class, () -> callback.block(5, TimeUnit.SECONDS));
        assertThat(error.getCause(), instanceOf(MessageTooLargeException.class));
        assertNull(endpoint.messages.poll());
    }

    @Test
    public void testValidUtf8() throws Exception
    {
        StringMessageSink messageSink = new StringMessageSink(coreSession, endpoint.getMethodHandle());
        ByteBuffer utf8Payload = BufferUtil.toBuffer(new byte[]{(byte)0xF0, (byte)0x90, (byte)0x8D, (byte)0x88});

        FutureCallback callback = new FutureCallback();
        messageSink.accept(new Frame(OpCode.TEXT, utf8Payload).setFin(true), callback);
        callback.block(5, TimeUnit.SECONDS);

        assertThat(endpoint.messages.poll(5, TimeUnit.SECONDS), is("\uD800\uDF48")); // UTF-8 encoded payload.
    }

    @Test
    public void testUtf8Continuation() throws Exception
    {
        StringMessageSink messageSink = new StringMessageSink(coreSession, endpoint.getMethodHandle());
        ByteBuffer firstUtf8Payload = BufferUtil.toBuffer(new byte[]{(byte)0xF0, (byte)0x90});
        ByteBuffer continuationUtf8Payload = BufferUtil.toBuffer(new byte[]{(byte)0x8D, (byte)0x88});

        FutureCallback callback = new FutureCallback();
        messageSink.accept(new Frame(OpCode.TEXT, firstUtf8Payload).setFin(false), callback);
        callback.block(5, TimeUnit.SECONDS);

        callback = new FutureCallback();
        messageSink.accept(new Frame(OpCode.TEXT, continuationUtf8Payload).setFin(true), callback);
        callback.block(5, TimeUnit.SECONDS);

        assertThat(endpoint.messages.poll(5, TimeUnit.SECONDS), is("\uD800\uDF48")); // UTF-8 encoded payload.
    }

    @Test
    public void testInvalidSingleFrameUtf8() throws Exception
    {
        StringMessageSink messageSink = new StringMessageSink(coreSession, endpoint.getMethodHandle());
        ByteBuffer invalidUtf8Payload = BufferUtil.toBuffer(new byte[]{(byte)0xF0, (byte)0x90, (byte)0x8D});

        FutureCallback callback = new FutureCallback();
        messageSink.accept(new Frame(OpCode.TEXT, invalidUtf8Payload).setFin(true), callback);

        // Callback should fail and we don't receive the message in the sink.
        RuntimeException error = assertThrows(RuntimeException.class, () -> callback.block(5, TimeUnit.SECONDS));
        assertThat(error.getCause(), instanceOf(Utf8Appendable.NotUtf8Exception.class));
        assertNull(endpoint.messages.poll());
    }

    @Test
    public void testInvalidMultiFrameUtf8() throws Exception
    {
        StringMessageSink messageSink = new StringMessageSink(coreSession, endpoint.getMethodHandle());
        ByteBuffer firstUtf8Payload = BufferUtil.toBuffer(new byte[]{(byte)0xF0, (byte)0x90});
        ByteBuffer continuationUtf8Payload = BufferUtil.toBuffer(new byte[]{(byte)0x8D});

        FutureCallback firstCallback = new FutureCallback();
        messageSink.accept(new Frame(OpCode.TEXT, firstUtf8Payload).setFin(false), firstCallback);
        firstCallback.block(5, TimeUnit.SECONDS);

        FutureCallback continuationCallback = new FutureCallback();
        messageSink.accept(new Frame(OpCode.TEXT, continuationUtf8Payload).setFin(true), continuationCallback);

        // Callback should fail and we don't receive the message in the sink.
        RuntimeException error = assertThrows(RuntimeException.class, () -> continuationCallback.block(5, TimeUnit.SECONDS));
        assertThat(error.getCause(), instanceOf(Utf8Appendable.NotUtf8Exception.class));
        assertNull(endpoint.messages.poll());
    }

    public static class OnMessageEndpoint
    {
        private BlockingArrayQueue<String> messages = new BlockingArrayQueue<>();

        public void onMessage(String message)
        {
            messages.add(message);
        }

        public MethodHandle getMethodHandle() throws Exception
        {
            return MethodHandles.lookup()
                .findVirtual(this.getClass(), "onMessage", MethodType.methodType(void.class, String.class))
                .bindTo(this);
        }
    }
}