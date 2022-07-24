package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;


import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.Hex;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.TypeUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Generator;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;

public class WebSocketFrameTest
{
    private final Generator generator = new Generator();

    private ByteBuffer generateWholeFrame(Generator generator, Frame frame)
    {
        ByteBuffer buf = BufferUtil.allocate(frame.getPayloadLength() + Generator.MAX_HEADER_LENGTH);
        generator.generateWholeFrame(frame, buf);
        return buf;
    }

    private void assertFrameHex(String message, String expectedHex, ByteBuffer actual)
    {
        String actualHex = Hex.asHex(actual);
        assertThat("Generated Frame:" + message, actualHex, is(expectedHex));
    }

    @Test
    public void testInvalidClose()
    {
        Frame frame = new Frame(OpCode.CLOSE).setFin(false);
        ByteBuffer actual = generateWholeFrame(generator, frame);
        String expected = "0800";
        assertFrameHex("Invalid Close Frame", expected, actual);
    }

    @Test
    public void testInvalidPing()
    {
        Frame frame = new Frame(OpCode.PING).setFin(false);
        ByteBuffer actual = generateWholeFrame(generator, frame);
        String expected = "0900";
        assertFrameHex("Invalid Ping Frame", expected, actual);
    }

    @Test
    public void testValidClose()
    {
        Frame frame = CloseStatus.toFrame(CloseStatus.NORMAL);
        ByteBuffer actual = generateWholeFrame(generator, frame);
        String expected = "880203E8";
        assertFrameHex("Valid Close Frame", expected, actual);
    }

    @Test
    public void testValidPing()
    {
        Frame frame = new Frame(OpCode.PING);
        ByteBuffer actual = generateWholeFrame(generator, frame);
        String expected = "8900";
        assertFrameHex("Valid Ping Frame", expected, actual);
    }

    @Test
    public void testRsv1()
    {
        Frame frame = new Frame(OpCode.TEXT);
        frame.setPayload("Hi");
        frame.setRsv1(true);
        ByteBuffer actual = generateWholeFrame(generator, frame);
        String expected = "C1024869";
        assertFrameHex("Text Frame with RSV1", expected, actual);
    }

    @Test
    public void testRsv2()
    {
        Frame frame = new Frame(OpCode.TEXT);
        frame.setPayload("Hi");
        frame.setRsv2(true);
        ByteBuffer actual = generateWholeFrame(generator, frame);
        String expected = "A1024869";
        assertFrameHex("Text Frame with RSV2", expected, actual);
    }

    @Test
    public void testRsv3()
    {
        Frame frame = new Frame(OpCode.TEXT);
        frame.setPayload("Hi");
        frame.setRsv3(true);
        ByteBuffer actual = generateWholeFrame(generator, frame);
        String expected = "91024869";
        assertFrameHex("Text Frame with RSV3", expected, actual);
    }

    @Test
    public void testDemask()
    {
        for (int i = 0; i <= 8; i++)
        {
            Frame frame = new Frame(OpCode.BINARY);
            frame.setPayload(TypeUtil.fromHexString("0000FFFF000FFFF0".substring(0, i * 2)));
            frame.setMask(TypeUtil.fromHexString("FF00FF00"));
            frame.demask();
            assertEquals("Ff0000FfFf0f00F0".substring(0, i * 2), BufferUtil.toHexString(frame.getPayload()), "len=" + i);
        }
    }
}