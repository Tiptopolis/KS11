package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;
import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
/**
 * {@link ContentDecoder} for the "gzip" encoding.
 */
public class GZIPContentDecoder extends com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.GZIPContentDecoder implements ContentDecoder
{
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public GZIPContentDecoder()
    {
        this(DEFAULT_BUFFER_SIZE);
    }

    public GZIPContentDecoder(int bufferSize)
    {
        this(null, bufferSize);
    }

    public GZIPContentDecoder(ByteBufferPool byteBufferPool, int bufferSize)
    {
        super(byteBufferPool, bufferSize);
    }

    @Override
    protected boolean decodedChunk(ByteBuffer chunk)
    {
        super.decodedChunk(chunk);
        return true;
    }

    /**
     * Specialized {@link ContentDecoder.Factory} for the "gzip" encoding.
     */
    public static class Factory extends ContentDecoder.Factory
    {
        private final int bufferSize;
        private final ByteBufferPool byteBufferPool;

        public Factory()
        {
            this(DEFAULT_BUFFER_SIZE);
        }

        public Factory(int bufferSize)
        {
            this(null, bufferSize);
        }

        public Factory(ByteBufferPool byteBufferPool)
        {
            this(byteBufferPool, DEFAULT_BUFFER_SIZE);
        }

        public Factory(ByteBufferPool byteBufferPool, int bufferSize)
        {
            super("gzip");
            this.byteBufferPool = byteBufferPool;
            this.bufferSize = bufferSize;
        }

        @Override
        public ContentDecoder newContentDecoder()
        {
            return new GZIPContentDecoder(byteBufferPool, bufferSize);
        }
    }
}