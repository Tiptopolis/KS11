package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.gzip;

import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.GZIPContentDecoder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.HttpInput;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.HttpInput.Content;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Destroyable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.InflaterPool;

/**
 * An HttpInput Interceptor that inflates GZIP encoded request content.
 */
public class GzipHttpInputInterceptor implements HttpInput.Interceptor, Destroyable
{
    private final Decoder _decoder;
    private ByteBuffer _chunk;
    public GzipHttpInputInterceptor(InflaterPool inflaterPool, ByteBufferPool pool, int bufferSize)
    {
        this(inflaterPool, pool, bufferSize, false);
    }

    public GzipHttpInputInterceptor(InflaterPool inflaterPool, ByteBufferPool pool, int bufferSize, boolean useDirectBuffers)
    {
        _decoder = new Decoder(inflaterPool, pool, bufferSize, useDirectBuffers);
    }

    @Override
    public Content readFrom(Content content)
    {
        if (content.isSpecial())
            return content;

        _decoder.decodeChunks(content.getByteBuffer());
        final ByteBuffer chunk = _chunk;

        if (chunk == null)
            return null;

        return new Content(chunk)
        {
            @Override
            public void succeeded()
            {
                _decoder.release(chunk);
            }

            @Override
            public void failed(Throwable x)
            {
                _decoder.release(chunk);
            }
        };
    }

    @Override
    public void destroy()
    {
        _decoder.destroy();
    }

    private class Decoder extends GZIPContentDecoder
    {
        private Decoder(InflaterPool inflaterPool, ByteBufferPool bufferPool, int bufferSize, boolean useDirectBuffers)
        {
            super(inflaterPool, bufferPool, bufferSize, useDirectBuffers);
        }

        @Override
        protected boolean decodedChunk(final ByteBuffer chunk)
        {
            _chunk = chunk;
            return true;
        }

        @Override
        public void decodeChunks(ByteBuffer compressed)
        {
            _chunk = null;
            super.decodeChunks(compressed);
        }
    }
}