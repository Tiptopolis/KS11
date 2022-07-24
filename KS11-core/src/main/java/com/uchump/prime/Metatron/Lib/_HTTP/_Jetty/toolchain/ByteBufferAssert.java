package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;

import java.nio.ByteBuffer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.CoreMatchers.is;
import static com.uchump.prime.Metatron.Lib._Hamcrest.CoreMatchers.nullValue;
import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;

public class ByteBufferAssert
{
    public static void assertEquals(String message, byte[] expected, byte[] actual)
    {
        assertThat(message + " byte[].length", actual.length, is(expected.length));
        int len = expected.length;
        for (int i = 0; i < len; i++)
        {
            assertThat(message + " byte[" + i + "]", actual[i], is(expected[i]));
        }
    }

    public static void assertEquals(String message, ByteBuffer expectedBuffer, ByteBuffer actualBuffer)
    {
        if (expectedBuffer == null)
        {
            assertThat(message, actualBuffer, nullValue());
        }
        else
        {
            byte expectedBytes[] = ByteBufferUtils.toArray(expectedBuffer);
            byte actualBytes[] = ByteBufferUtils.toArray(actualBuffer);
            assertEquals(message, expectedBytes, actualBytes);
        }
    }

    public static void assertEquals(String message, String expectedString, ByteBuffer actualBuffer)
    {
        String actualString = ByteBufferUtils.toString(actualBuffer);
        assertThat(message, actualString, is(expectedString));
    }

    public static void assertSize(String message, int expectedSize, ByteBuffer buffer)
    {
        if ((expectedSize == 0) && (buffer == null))
        {
            return;
        }
        assertThat(message + " buffer.remaining", buffer.remaining(), is(expectedSize));
    }
}