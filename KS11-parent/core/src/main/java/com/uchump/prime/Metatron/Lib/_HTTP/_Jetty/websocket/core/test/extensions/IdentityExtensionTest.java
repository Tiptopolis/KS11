package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.extensions;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.ByteBufferAssert;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Extension;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.IdentityExtension;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.IncomingFramesCapture;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.OutgoingFramesCapture;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

public class IdentityExtensionTest extends AbstractExtensionTest
{
    /**
     * Verify that incoming frames are unmodified
     */
    @Test
    public void testIncomingFrames()
    {
        IncomingFramesCapture capture = new IncomingFramesCapture();

        Extension ext = new IdentityExtension();
        ext.setNextIncomingFrames(capture);

        Frame frame = new Frame(OpCode.TEXT).setPayload("hello");
        ext.onFrame(frame, Callback.NOOP);

        capture.assertFrameCount(1);
        capture.assertHasOpCount(OpCode.TEXT, 1);
        Frame actual = capture.frames.poll();

        assertThat("Frame.opcode", actual.getOpCode(), is(OpCode.TEXT));
        assertThat("Frame.fin", actual.isFin(), is(true));
        assertThat("Frame.rsv1", actual.isRsv1(), is(false));
        assertThat("Frame.rsv2", actual.isRsv2(), is(false));
        assertThat("Frame.rsv3", actual.isRsv3(), is(false));

        ByteBuffer expected = BufferUtil.toBuffer("hello", StandardCharsets.UTF_8);
        assertThat("Frame.payloadLength", actual.getPayloadLength(), is(expected.remaining()));
        ByteBufferAssert.assertEquals("Frame.payload", expected, actual.getPayload().slice());
    }

    /**
     * Verify that outgoing frames are unmodified
     *
     * @throws IOException on test failure
     */
    @Test
    public void testOutgoingFrames() throws IOException
    {
        OutgoingFramesCapture capture = new OutgoingFramesCapture();

        Extension ext = new IdentityExtension();
        ext.setNextOutgoingFrames(capture);

        Frame frame = new Frame(OpCode.TEXT).setPayload("hello");
        ext.sendFrame(frame, null, false);

        capture.assertFrameCount(1);
        capture.assertHasOpCount(OpCode.TEXT, 1);

        Frame actual = capture.frames.poll();

        assertThat("Frame.opcode", actual.getOpCode(), is(OpCode.TEXT));
        assertThat("Frame.fin", actual.isFin(), is(true));
        assertThat("Frame.rsv1", actual.isRsv1(), is(false));
        assertThat("Frame.rsv2", actual.isRsv2(), is(false));
        assertThat("Frame.rsv3", actual.isRsv3(), is(false));

        ByteBuffer expected = BufferUtil.toBuffer("hello", StandardCharsets.UTF_8);
        assertThat("Frame.payloadLength", actual.getPayloadLength(), is(expected.remaining()));
        ByteBufferAssert.assertEquals("Frame.payload", expected, actual.getPayload().slice());
    }
}