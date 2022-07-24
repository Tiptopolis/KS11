package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util;


import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;


/**
 * A {@link Request.Content} for byte arrays.
 */
public class BytesRequestContent extends AbstractRequestContent
{
    private final byte[][] bytes;
    private final long length;

    public BytesRequestContent(byte[]... bytes)
    {
        this("application/octet-stream", bytes);
    }

    public BytesRequestContent(String contentType, byte[]... bytes)
    {
        super(contentType);
        this.bytes = bytes;
        this.length = Arrays.stream(bytes).mapToLong(a -> a.length).sum();
    }

    @Override
    public long getLength()
    {
        return length;
    }

    @Override
    public boolean isReproducible()
    {
        return true;
    }

    @Override
    protected Subscription newSubscription(Consumer consumer, boolean emitInitialContent)
    {
        return new SubscriptionImpl(consumer, emitInitialContent);
    }

    private class SubscriptionImpl extends AbstractSubscription
    {
        private int index;

        private SubscriptionImpl(Consumer consumer, boolean emitInitialContent)
        {
            super(consumer, emitInitialContent);
        }

        @Override
        protected boolean produceContent(Producer producer) throws IOException
        {
            if (index < 0)
                throw new EOFException("Demand after last content");
            ByteBuffer buffer = BufferUtil.EMPTY_BUFFER;
            if (index < bytes.length)
                buffer = ByteBuffer.wrap(bytes[index++]);
            boolean lastContent = index == bytes.length;
            if (lastContent)
                index = -1;
            return producer.produce(buffer, lastContent, Callback.NOOP);
        }
    }
}