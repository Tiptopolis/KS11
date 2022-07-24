package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.MappedByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging.StacklessLogging;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.ProtocolException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Parser;

import static java.nio.charset.StandardCharsets.UTF_8;
import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test behavior of Parser when encountering bad / forbidden opcodes (per RFC6455)
 */
public class ParserBadOpCodesTest
{
    public static Stream<Arguments> data()
    {
        return Stream.of(
            Arguments.of((byte)3, "Autobahn Server Testcase 4.1.1"),
            Arguments.of((byte)4, "Autobahn Server Testcase 4.1.2"),
            Arguments.of((byte)5, "Autobahn Server Testcase 4.1.3"),
            Arguments.of((byte)6, "Autobahn Server Testcase 4.1.4"),
            Arguments.of((byte)7, "Autobahn Server Testcase 4.1.5"),
            Arguments.of((byte)11, "Autobahn Server Testcase 4.2.1"),
            Arguments.of((byte)12, "Autobahn Server Testcase 4.2.2"),
            Arguments.of((byte)13, "Autobahn Server Testcase 4.2.3"),
            Arguments.of((byte)14, "Autobahn Server Testcase 4.2.4"),
            Arguments.of((byte)15, "Autobahn Server Testcase 4.2.5")
        );
    }

    private final ByteBufferPool bufferPool = new MappedByteBufferPool();

    @ParameterizedTest(name = "opcode={0} {1}")
    @MethodSource("data")
    public void testBadOpCode(byte opcode, String description)
    {
        ParserCapture capture = new ParserCapture();

        ByteBuffer raw = BufferUtil.allocate(256);
        BufferUtil.flipToFill(raw);

        // add bad opcode frame
        RawFrameBuilder.putOpFin(raw, opcode, true);
        RawFrameBuilder.putLength(raw, 0, false);

        // parse buffer
        BufferUtil.flipToFlush(raw, 0);
        try (StacklessLogging ignore = new StacklessLogging(Parser.class))
        {
            Exception e = assertThrows(ProtocolException.class, () -> capture.parse(raw));
            assertThat(e.getMessage(), containsString("Unknown opcode: " + opcode));
        }
    }

    @ParameterizedTest(name = "opcode={0} {1}")
    @MethodSource("data")
    public void testTextBadOpCodePing(byte opcode, String description)
    {
        ParserCapture capture = new ParserCapture();

        ByteBuffer raw = BufferUtil.allocate(256);
        BufferUtil.flipToFill(raw);

        // adding text frame
        ByteBuffer msg = BufferUtil.toBuffer("hello", UTF_8);
        RawFrameBuilder.putOpFin(raw, OpCode.TEXT, true);
        RawFrameBuilder.putLength(raw, msg.remaining(), false);
        BufferUtil.put(msg, raw);

        // adding bad opcode frame
        RawFrameBuilder.putOpFin(raw, opcode, true);
        RawFrameBuilder.putLength(raw, 0, false);

        // adding ping frame
        RawFrameBuilder.putOpFin(raw, OpCode.PING, true);
        RawFrameBuilder.putLength(raw, 0, false);

        // parse provided buffer
        BufferUtil.flipToFlush(raw, 0);
        try (StacklessLogging ignore = new StacklessLogging(Parser.class))
        {
            Exception e = assertThrows(ProtocolException.class, () -> capture.parse(raw));
            assertThat(e.getMessage(), containsString("Unknown opcode: " + opcode));
        }
    }
}