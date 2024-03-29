package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util;


import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IO;

/**
 * <p>A {@link Request.Content} for files using JDK 7's {@code java.nio.file} APIs.</p>
 * <p>It is possible to specify, at the constructor, a buffer size used to read
 * content from the stream, by default 4096 bytes.
 * If a {@link ByteBufferPool} is provided via {@link #setByteBufferPool(ByteBufferPool)},
 * the buffer will be allocated from that pool, otherwise one buffer will be
 * allocated and used to read the file.</p>
 */
public class PathRequestContent extends AbstractRequestContent
{
    private static final Logger LOG = LoggerFactory.getLogger(PathRequestContent.class);

    private final Path filePath;
    private final long fileSize;
    private final int bufferSize;
    private ByteBufferPool bufferPool;
    private boolean useDirectByteBuffers = true;

    public PathRequestContent(Path filePath) throws IOException
    {
        this(filePath, 4096);
    }

    public PathRequestContent(Path filePath, int bufferSize) throws IOException
    {
        this("application/octet-stream", filePath, bufferSize);
    }

    public PathRequestContent(String contentType, Path filePath) throws IOException
    {
        this(contentType, filePath, 4096);
    }

    public PathRequestContent(String contentType, Path filePath, int bufferSize) throws IOException
    {
        super(contentType);
        if (!Files.isRegularFile(filePath))
            throw new NoSuchFileException(filePath.toString());
        if (!Files.isReadable(filePath))
            throw new AccessDeniedException(filePath.toString());
        this.filePath = filePath;
        this.fileSize = Files.size(filePath);
        this.bufferSize = bufferSize;
    }

    @Override
    public long getLength()
    {
        return fileSize;
    }

    @Override
    public boolean isReproducible()
    {
        return true;
    }

    public ByteBufferPool getByteBufferPool()
    {
        return bufferPool;
    }

    public void setByteBufferPool(ByteBufferPool byteBufferPool)
    {
        this.bufferPool = byteBufferPool;
    }

    public boolean isUseDirectByteBuffers()
    {
        return useDirectByteBuffers;
    }

    public void setUseDirectByteBuffers(boolean useDirectByteBuffers)
    {
        this.useDirectByteBuffers = useDirectByteBuffers;
    }

    @Override
    protected Subscription newSubscription(Consumer consumer, boolean emitInitialContent)
    {
        return new SubscriptionImpl(consumer, emitInitialContent);
    }

    private class SubscriptionImpl extends AbstractSubscription
    {
        private ReadableByteChannel channel;
        private long readTotal;

        private SubscriptionImpl(Consumer consumer, boolean emitInitialContent)
        {
            super(consumer, emitInitialContent);
        }

        @Override
        protected boolean produceContent(Producer producer) throws IOException
        {
            ByteBuffer buffer;
            boolean last;
            if (channel == null)
            {
                channel = Files.newByteChannel(filePath, StandardOpenOption.READ);
                if (LOG.isDebugEnabled())
                    LOG.debug("Opened file {}", filePath);
            }

            buffer = bufferPool == null
                ? BufferUtil.allocate(bufferSize, isUseDirectByteBuffers())
                : bufferPool.acquire(bufferSize, isUseDirectByteBuffers());

            BufferUtil.clearToFill(buffer);
            int read = channel.read(buffer);
            BufferUtil.flipToFlush(buffer, 0);
            if (LOG.isDebugEnabled())
                LOG.debug("Read {} bytes from {}", read, filePath);
            if (!channel.isOpen() && read < 0)
                throw new EOFException("EOF reached for " + filePath);

            if (read > 0)
                readTotal += read;
            last = readTotal == fileSize;
            if (last)
                IO.close(channel);
            return producer.produce(buffer, last, Callback.from(() -> release(buffer)));
        }

        private void release(ByteBuffer buffer)
        {
            if (bufferPool != null)
                bufferPool.release(buffer);
        }

        @Override
        public void fail(Throwable failure)
        {
            super.fail(failure);
            IO.close(channel);
        }
    }
}