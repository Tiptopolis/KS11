package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.MalformedInputException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.FutureCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IO;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.StringUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.MessageReader;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageReaderTest
{
    private final MessageReader reader = new MessageReader();
    private final CompletableFuture<String> message = new CompletableFuture<>();
    private boolean first = true;

    @BeforeEach
    public void before()
    {
        // Read the message in a different thread.
        new Thread(() ->
        {
            try
            {
                message.complete(IO.toString(reader));
            }
            catch (IOException e)
            {
                message.completeExceptionally(e);
            }
        }).start();
    }

    @Test
    public void testSingleFrameMessage() throws Exception
    {
        giveString("hello world!", true);

        String s = message.get(5, TimeUnit.SECONDS);
        assertThat(s, is("hello world!"));
    }

    @Test
    public void testFragmentedMessage() throws Exception
    {
        giveString("hello", false);
        giveString(" ", false);
        giveString("world", false);
        giveString("!", true);

        String s = message.get(5, TimeUnit.SECONDS);
        assertThat(s, is("hello world!"));
    }

    @Test
    public void testEmptySegments() throws Exception
    {
        giveString("", false);
        giveString("hello ", false);
        giveString("", false);
        giveString("", false);
        giveString("world!", false);
        giveString("", false);
        giveString("", true);

        String s = message.get(5, TimeUnit.SECONDS);
        assertThat(s, is("hello world!"));
    }

    @Test
    public void testCloseStream() throws Exception
    {
        giveString("hello ", false);
        reader.close();
        giveString("world!", true);

        ExecutionException error = assertThrows(ExecutionException.class, () -> message.get(5, TimeUnit.SECONDS));
        Throwable cause = error.getCause();
        assertThat(cause, instanceOf(IOException.class));
        assertThat(cause.getMessage(), is("Closed"));
    }

    @Test
    public void testInvalidUtf8() throws Exception
    {
        ByteBuffer invalidUtf8Payload = BufferUtil.toBuffer(new byte[]{0x7F, (byte)0xFF, (byte)0xFF});
        giveByteBuffer(invalidUtf8Payload, true);

        ExecutionException error = assertThrows(ExecutionException.class, () -> message.get(5, TimeUnit.SECONDS));
        assertThat(error.getCause(), instanceOf(MalformedInputException.class));
    }

    private void giveString(String s, boolean last) throws IOException
    {
        giveByteBuffer(ByteBuffer.wrap(StringUtil.getUtf8Bytes(s)), last);
    }

    private void giveByteBuffer(ByteBuffer buffer, boolean last) throws IOException
    {
        byte opCode = first ? OpCode.TEXT : OpCode.CONTINUATION;
        Frame frame = new Frame(opCode, last, buffer);
        FutureCallback callback = new FutureCallback();
        reader.accept(frame, callback);
        callback.block(5, TimeUnit.SECONDS);
        first = false;
    }
}