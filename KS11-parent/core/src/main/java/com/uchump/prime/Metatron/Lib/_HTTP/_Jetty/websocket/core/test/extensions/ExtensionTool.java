package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.extensions;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Assertions;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.MappedByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.ByteBufferAssert;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.FutureCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.TypeUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Behavior;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Extension;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.ExtensionStack;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Negotiated;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Parser;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.IncomingFramesCapture;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.TestMessageHandler;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matchers;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.equalTo;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

public class ExtensionTool
{
    public class Tester
    {
        private final String requestedExtParams;
        private final ExtensionConfig extConfig;
        private Extension ext;
        private final Parser parser;
        private final IncomingFramesCapture capture;
        private final WebSocketCoreSession coreSession;

        private Tester(String parameterizedExtension)
        {
            this.requestedExtParams = parameterizedExtension;
            this.extConfig = ExtensionConfig.parse(parameterizedExtension);
            coreSession = newWebSocketCoreSession(Collections.singletonList(extConfig));
            ExtensionStack extensionStack = coreSession.getExtensionStack();
            assertThat(extensionStack.getExtensions().size(), equalTo(1));

            this.capture = new IncomingFramesCapture();
            this.parser = new Parser(new MappedByteBufferPool());
        }

        public String getRequestedExtParams()
        {
            return requestedExtParams;
        }

        public void assertNegotiated(String expectedNegotiation)
        {
            this.ext = coreSession.getExtensionStack().getExtensions().get(0);
            this.ext.setNextIncomingFrames(capture);
        }

        public void parseIncomingHex(String... rawhex)
        {
            int parts = rawhex.length;
            byte[] net;

            // Simulate initial demand from onOpen().
            coreSession.autoDemand();

            for (int i = 0; i < parts; i++)
            {
                String hex = rawhex[i].replaceAll("\\s*(0x)?", "");
                net = TypeUtil.fromHexString(hex);

                ByteBuffer buffer = ByteBuffer.wrap(net);
                while (BufferUtil.hasContent(buffer))
                {
                    Frame frame = parser.parse(buffer);
                    if (frame == null)
                        break;

                    FutureCallback callback = new FutureCallback()
                    {
                        @Override
                        public void succeeded()
                        {
                            super.succeeded();
                            if (!coreSession.isDemanding())
                                coreSession.autoDemand();
                        }
                    };
                    ext.onFrame(frame, callback);

                    // Throw if callback fails.
                    try
                    {
                        callback.get();
                    }
                    catch (ExecutionException e)
                    {
                        throw new RuntimeException(e.getCause());
                    }
                    catch (Throwable t)
                    {
                        Assertions.fail(t);
                    }
                }
            }
        }

        public void assertHasFrames(String... textFrames)
        {
            Frame[] frames = new Frame[textFrames.length];
            for (int i = 0; i < frames.length; i++)
            {
                frames[i] = new Frame(OpCode.TEXT).setPayload(textFrames[i]);
            }
            assertHasFrames(frames);
        }

        public void assertHasFrames(Frame... expectedFrames)
        {
            int expectedCount = expectedFrames.length;
            assertThat("Frame Count", capture.frames.size(), is(expectedCount));

            for (int i = 0; i < expectedCount; i++)
            {
                Frame actual = capture.frames.poll();

                String prefix = String.format("frame[%d]", i);
                assertThat(prefix + ".opcode", actual.getOpCode(), Matchers.is(expectedFrames[i].getOpCode()));
                assertThat(prefix + ".fin", actual.isFin(), Matchers.is(expectedFrames[i].isFin()));
                assertThat(prefix + ".rsv1", actual.isRsv1(), is(false));
                assertThat(prefix + ".rsv2", actual.isRsv2(), is(false));
                assertThat(prefix + ".rsv3", actual.isRsv3(), is(false));

                ByteBuffer expected = expectedFrames[i].getPayload().slice();
                assertThat(prefix + ".payloadLength", actual.getPayloadLength(), is(expected.remaining()));
                ByteBufferAssert.assertEquals(prefix + ".payload", expected, actual.getPayload().slice());
            }
        }
    }

    private final WebSocketComponents components;

    public ExtensionTool(ByteBufferPool bufferPool)
    {
        this.components = new WebSocketComponents();
    }

    public Tester newTester(String parameterizedExtension)
    {
        return new Tester(parameterizedExtension);
    }

    private WebSocketCoreSession newWebSocketCoreSession(List<ExtensionConfig> configs)
    {
        ExtensionStack exStack = new ExtensionStack(components, Behavior.SERVER);
        exStack.setLastDemand(l -> {}); // Never delegate to WebSocketConnection as it is null for this test.
        exStack.negotiate(configs, configs);
        return new WebSocketCoreSession(new TestMessageHandler(), Behavior.SERVER, Negotiated.from(exStack), components);
    }
}