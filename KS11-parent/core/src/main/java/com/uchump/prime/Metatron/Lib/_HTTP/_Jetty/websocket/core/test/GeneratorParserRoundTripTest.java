package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.MappedByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Behavior;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Generator;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneratorParserRoundTripTest
{
    private final ByteBufferPool bufferPool = new MappedByteBufferPool();

    @Test
    public void testParserAndGenerator() throws Exception
    {
        Generator gen = new Generator();
        ParserCapture capture = new ParserCapture();

        String message = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";

        ByteBuffer out = bufferPool.acquire(8192, false);
        try
        {
            // Generate Buffer
            Frame frame = new Frame(OpCode.TEXT).setPayload(message);
            gen.generateHeader(frame, out);
            gen.generatePayload(frame, out);

            // Parse Buffer
            capture.parse(out);
        }
        finally
        {
            bufferPool.release(out);
        }

        // Validate
        Frame txt = capture.framesQueue.poll(1, TimeUnit.SECONDS);
        assertThat("Text parsed", txt.getPayloadAsUTF8(), is(message));
    }

    @Test
    public void testParserAndGeneratorMasked() throws Exception
    {
        Generator gen = new Generator();
        ParserCapture capture = new ParserCapture(true, Behavior.SERVER);

        String message = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";

        ByteBuffer out = bufferPool.acquire(8192, false);
        try
        {
            // Setup Frame
            Frame frame = new Frame(OpCode.TEXT).setPayload(message);

            // Add masking
            byte[] mask = new byte[4];
            Arrays.fill(mask, (byte)0xFF);
            frame.setMask(mask);

            // Generate Buffer
            gen.generateHeader(frame, out);
            gen.generatePayload(frame, out);

            // Parse Buffer
            capture.parse(out);
        }
        finally
        {
            bufferPool.release(out);
        }

        // Validate
        Frame txt = capture.framesQueue.poll(1, TimeUnit.SECONDS);
        assertTrue(txt.isMasked(), "Text.isMasked");
        assertThat("Text parsed", txt.getPayloadAsUTF8(), is(message));
    }
}