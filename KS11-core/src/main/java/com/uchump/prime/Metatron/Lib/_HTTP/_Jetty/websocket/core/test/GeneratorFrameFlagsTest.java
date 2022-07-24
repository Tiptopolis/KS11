package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;


import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.stream.Stream;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Behavior;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.ProtocolException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.ExtensionStack;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Generator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Negotiated;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.FrameValidation;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Test various invalid frame situations
 */
public class GeneratorFrameFlagsTest
{
    private static final WebSocketComponents components = new WebSocketComponents();
    private WebSocketCoreSession coreSession;

    public static Stream<Arguments> data()
    {
        return Stream.of(
            Arguments.of(new Frame(OpCode.PING).setFin(false)),
            Arguments.of(new Frame(OpCode.PING).setRsv1(true)),
            Arguments.of(new Frame(OpCode.PING).setRsv2(true)),
            Arguments.of(new Frame(OpCode.PING).setRsv3(true)),
            Arguments.of(new Frame(OpCode.PONG).setFin(false)),
            Arguments.of(new Frame(OpCode.PING).setRsv1(true)),
            Arguments.of(new Frame(OpCode.PONG).setRsv2(true)),
            Arguments.of(new Frame(OpCode.PONG).setRsv3(true)),
            Arguments.of(new Frame(OpCode.CLOSE).setFin(false)),
            Arguments.of(new Frame(OpCode.CLOSE).setRsv1(true)),
            Arguments.of(new Frame(OpCode.CLOSE).setRsv2(true)),
            Arguments.of(new Frame(OpCode.CLOSE).setRsv3(true))
        );
    }

    public void setup(Frame invalidFrame)
    {
        ExtensionStack exStack = new ExtensionStack(components, Behavior.SERVER);
        exStack.negotiate(new LinkedList<>(), new LinkedList<>());
        this.coreSession = new WebSocketCoreSession(new TestMessageHandler(), Behavior.CLIENT, Negotiated.from(exStack), components);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGenerateInvalidControlFrame(Frame invalidFrame)
    {
        setup(invalidFrame);

        ByteBuffer buffer = BufferUtil.allocate(100);
        new Generator().generateWholeFrame(invalidFrame, buffer);
        assertThrows(ProtocolException.class, () -> FrameValidation.assertValidOutgoing(invalidFrame, coreSession));
    }
}