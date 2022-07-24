package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class DataUtils
{
    /**
     * Make a copy of a byte buffer.
     * <p>
     * This is important in some tests, as the underlying byte buffer contained in a Frame can be modified through
     * masking and make it difficult to compare the results in the fuzzer.
     *
     * @param payload the payload to copy
     * @return a new byte array of the payload contents
     */
    public static ByteBuffer copyOf(ByteBuffer payload)
    {
        if (payload == null)
            return null;

        ByteBuffer copy = ByteBuffer.allocate(payload.remaining());
        copy.put(payload.slice());
        copy.flip();
        return copy;
    }

    /**
     * Make a copy of a byte buffer.
     * <p>
     * This is important in some tests, as the underlying byte buffer contained in a Frame can be modified through
     * masking and make it difficult to compare the results in the fuzzer.
     *
     * @param payload the payload to copy
     * @return a new byte array of the payload contents
     */
    public static ByteBuffer copyOf(byte[] payload)
    {
        return ByteBuffer.wrap(Arrays.copyOf(payload, payload.length));
    }
}