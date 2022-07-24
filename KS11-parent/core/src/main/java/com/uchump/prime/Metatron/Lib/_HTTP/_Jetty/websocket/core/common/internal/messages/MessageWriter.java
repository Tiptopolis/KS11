package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages;

import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Support for writing a single WebSocket TEXT message via a {@link Writer}
 * <p>
 * Note: Per WebSocket spec, all WebSocket TEXT messages must be encoded in UTF-8
 */
public class MessageWriter extends Writer
{
    private final MessageOutputStream outputStream;
    private final CharsetEncoder utf8Encoder = UTF_8.newEncoder()
        .onUnmappableCharacter(CodingErrorAction.REPORT)
        .onMalformedInput(CodingErrorAction.REPORT);

    public MessageWriter(CoreSession coreSession, ByteBufferPool bufferPool)
    {
        this.outputStream = new MessageOutputStream(coreSession, bufferPool);
        this.outputStream.setMessageType(OpCode.TEXT);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException
    {
        CharBuffer charBuffer = CharBuffer.wrap(cbuf, off, len);
        outputStream.write(utf8Encoder.encode(charBuffer));
    }

    @Override
    public void flush() throws IOException
    {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException
    {
        outputStream.close();
    }

    public void setCallback(Callback callback)
    {
        outputStream.setCallback(callback);
    }
}