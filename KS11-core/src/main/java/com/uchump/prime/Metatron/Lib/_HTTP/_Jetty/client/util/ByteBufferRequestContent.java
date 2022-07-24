package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util;


import java.io.EOFException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;

/**
 * <p>A {@link Request.Content} for {@link ByteBuffer}s.</p>
 * <p>The position and limit of the {@link ByteBuffer}s passed to the constructor are not modified;
 * content production returns a {@link ByteBuffer#slice() slice} of the original {@link ByteBuffer}.
 */
public class ByteBufferRequestContent extends AbstractRequestContent
{
    private final ByteBuffer[] buffers;
    private final long length;

    public ByteBufferRequestContent(ByteBuffer... buffers)
    {
        this("application/octet-stream", buffers);
    }

    public ByteBufferRequestContent(String contentType, ByteBuffer... buffers)
    {
        super(contentType);
        this.buffers = buffers;
        this.length = Arrays.stream(buffers).mapToLong(Buffer::remaining).sum();
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
            if (index < buffers.length)
                buffer = buffers[index++];
            boolean lastContent = index == buffers.length;
            if (lastContent)
                index = -1;
            return producer.produce(buffer.slice(), lastContent, Callback.NOOP);
        }
    }
}