package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.http;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpExchange;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequestException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpSender;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpGenerator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.MetaData;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IteratingCallback;

public class HttpSenderOverHTTP extends HttpSender
{
    private static final Logger LOG = LoggerFactory.getLogger(HttpSenderOverHTTP.class);

    private final IteratingCallback headersCallback = new HeadersCallback();
    private final IteratingCallback contentCallback = new ContentCallback();
    private final HttpGenerator generator = new HttpGenerator();
    private HttpExchange exchange;
    private MetaData.Request metaData;
    private ByteBuffer contentBuffer;
    private boolean lastContent;
    private Callback callback;
    private boolean shutdown;

    public HttpSenderOverHTTP(HttpChannelOverHTTP channel)
    {
        super(channel);
    }

    @Override
    public HttpChannelOverHTTP getHttpChannel()
    {
        return (HttpChannelOverHTTP)super.getHttpChannel();
    }

    @Override
    protected void sendHeaders(HttpExchange exchange, ByteBuffer contentBuffer, boolean lastContent, Callback callback)
    {
        try
        {
            this.exchange = exchange;
            this.contentBuffer = contentBuffer;
            this.lastContent = lastContent;
            this.callback = callback;
            HttpRequest request = exchange.getRequest();
            Request.Content requestContent = request.getBody();
            long contentLength = requestContent == null ? -1 : requestContent.getLength();
            String path = request.getPath();
            String query = request.getQuery();
            if (query != null)
                path += "?" + query;
            metaData = new MetaData.Request(request.getMethod(), HttpURI.from(path), request.getVersion(), request.getHeaders(), contentLength, request.getTrailers());
            if (LOG.isDebugEnabled())
                LOG.debug("Sending headers with content {} last={} for {}", BufferUtil.toDetailString(contentBuffer), lastContent, exchange.getRequest());
            headersCallback.iterate();
        }
        catch (Throwable x)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Unable to send headers on exchange {}", exchange, x);
            callback.failed(x);
        }
    }

    @Override
    protected void sendContent(HttpExchange exchange, ByteBuffer contentBuffer, boolean lastContent, Callback callback)
    {
        try
        {
            this.exchange = exchange;
            this.contentBuffer = contentBuffer;
            this.lastContent = lastContent;
            this.callback = callback;
            if (LOG.isDebugEnabled())
                LOG.debug("Sending content {} last={} for {}", BufferUtil.toDetailString(contentBuffer), lastContent, exchange.getRequest());
            contentCallback.iterate();
        }
        catch (Throwable x)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Unable to send content on {}", exchange, x);
            callback.failed(x);
        }
    }

    @Override
    protected void reset()
    {
        headersCallback.reset();
        contentCallback.reset();
        generator.reset();
        super.reset();
    }

    @Override
    protected void dispose()
    {
        generator.abort();
        super.dispose();
        shutdownOutput();
    }

    private void shutdownOutput()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Request shutdown output {}", getHttpExchange().getRequest());
        shutdown = true;
    }

    protected boolean isShutdown()
    {
        return shutdown;
    }

    @Override
    public String toString()
    {
        return String.format("%s[%s]", super.toString(), generator);
    }

    private class HeadersCallback extends IteratingCallback
    {
        private ByteBuffer headerBuffer;
        private ByteBuffer chunkBuffer;
        private boolean generated;

        private HeadersCallback()
        {
            super(false);
        }

        @Override
        protected Action process() throws Exception
        {
            HttpClient httpClient = getHttpChannel().getHttpDestination().getHttpClient();
            ByteBufferPool byteBufferPool = httpClient.getByteBufferPool();
            boolean useDirectByteBuffers = httpClient.isUseOutputDirectByteBuffers();
            while (true)
            {
                HttpGenerator.Result result = generator.generateRequest(metaData, headerBuffer, chunkBuffer, contentBuffer, lastContent);
                if (LOG.isDebugEnabled())
                    LOG.debug("Generated headers ({} bytes), chunk ({} bytes), content ({} bytes) - {}/{} for {}",
                        headerBuffer == null ? -1 : headerBuffer.remaining(),
                        chunkBuffer == null ? -1 : chunkBuffer.remaining(),
                        contentBuffer == null ? -1 : contentBuffer.remaining(),
                        result, generator, exchange.getRequest());
                switch (result)
                {
                    case NEED_HEADER:
                    {
                        headerBuffer = byteBufferPool.acquire(httpClient.getRequestBufferSize(), useDirectByteBuffers);
                        break;
                    }
                    case HEADER_OVERFLOW:
                    {
                        httpClient.getByteBufferPool().release(headerBuffer);
                        headerBuffer = null;
                        throw new IllegalArgumentException("Request header too large");
                    }
                    case NEED_CHUNK:
                    {
                        chunkBuffer = byteBufferPool.acquire(HttpGenerator.CHUNK_SIZE, useDirectByteBuffers);
                        break;
                    }
                    case NEED_CHUNK_TRAILER:
                    {
                        chunkBuffer = byteBufferPool.acquire(httpClient.getRequestBufferSize(), useDirectByteBuffers);
                        break;
                    }
                    case FLUSH:
                    {
                        EndPoint endPoint = getHttpChannel().getHttpConnection().getEndPoint();
                        if (headerBuffer == null)
                            headerBuffer = BufferUtil.EMPTY_BUFFER;
                        if (chunkBuffer == null)
                            chunkBuffer = BufferUtil.EMPTY_BUFFER;
                        if (contentBuffer == null)
                            contentBuffer = BufferUtil.EMPTY_BUFFER;
                        long bytes = headerBuffer.remaining() + chunkBuffer.remaining() + contentBuffer.remaining();
                        getHttpChannel().getHttpConnection().addBytesOut(bytes);
                        endPoint.write(this, headerBuffer, chunkBuffer, contentBuffer);
                        generated = true;
                        return Action.SCHEDULED;
                    }
                    case SHUTDOWN_OUT:
                    {
                        shutdownOutput();
                        return Action.SUCCEEDED;
                    }
                    case CONTINUE:
                    {
                        if (generated)
                            return Action.SUCCEEDED;
                        break;
                    }
                    case DONE:
                    {
                        if (generated)
                            return Action.SUCCEEDED;
                        // The headers have already been generated by some
                        // other thread, perhaps by a concurrent abort().
                        throw new HttpRequestException("Could not generate headers", exchange.getRequest());
                    }
                    default:
                    {
                        throw new IllegalStateException(result.toString());
                    }
                }
            }
        }

        @Override
        public void succeeded()
        {
            release();
            super.succeeded();
        }

        @Override
        public void failed(Throwable x)
        {
            release();
            super.failed(x);
        }

        @Override
        protected void onCompleteSuccess()
        {
            super.onCompleteSuccess();
            callback.succeeded();
        }

        @Override
        protected void onCompleteFailure(Throwable cause)
        {
            super.onCompleteFailure(cause);
            callback.failed(cause);
        }

        private void release()
        {
            HttpClient httpClient = getHttpChannel().getHttpDestination().getHttpClient();
            ByteBufferPool bufferPool = httpClient.getByteBufferPool();
            if (!BufferUtil.isTheEmptyBuffer(headerBuffer))
                bufferPool.release(headerBuffer);
            headerBuffer = null;
            if (!BufferUtil.isTheEmptyBuffer(chunkBuffer))
                bufferPool.release(chunkBuffer);
            chunkBuffer = null;
            contentBuffer = null;
        }
    }

    private class ContentCallback extends IteratingCallback
    {
        private ByteBuffer chunkBuffer;

        public ContentCallback()
        {
            super(false);
        }

        @Override
        protected Action process() throws Exception
        {
            HttpClient httpClient = getHttpChannel().getHttpDestination().getHttpClient();
            ByteBufferPool bufferPool = httpClient.getByteBufferPool();
            boolean useDirectByteBuffers = httpClient.isUseOutputDirectByteBuffers();
            while (true)
            {
                HttpGenerator.Result result = generator.generateRequest(null, null, chunkBuffer, contentBuffer, lastContent);
                if (LOG.isDebugEnabled())
                    LOG.debug("Generated content ({} bytes, last={}) - {}/{}",
                        contentBuffer == null ? -1 : contentBuffer.remaining(),
                        lastContent, result, generator);
                switch (result)
                {
                    case NEED_CHUNK:
                    {
                        chunkBuffer = bufferPool.acquire(HttpGenerator.CHUNK_SIZE, useDirectByteBuffers);
                        break;
                    }
                    case NEED_CHUNK_TRAILER:
                    {
                        chunkBuffer = bufferPool.acquire(httpClient.getRequestBufferSize(), useDirectByteBuffers);
                        break;
                    }
                    case FLUSH:
                    {
                        EndPoint endPoint = getHttpChannel().getHttpConnection().getEndPoint();
                        if (chunkBuffer != null)
                            endPoint.write(this, chunkBuffer, contentBuffer);
                        else
                            endPoint.write(this, contentBuffer);
                        return Action.SCHEDULED;
                    }
                    case SHUTDOWN_OUT:
                    {
                        shutdownOutput();
                        break;
                    }
                    case CONTINUE:
                    {
                        break;
                    }
                    case DONE:
                    {
                        release();
                        callback.succeeded();
                        return Action.IDLE;
                    }
                    default:
                    {
                        throw new IllegalStateException(result.toString());
                    }
                }
            }
        }

        @Override
        protected void onCompleteFailure(Throwable cause)
        {
            release();
            callback.failed(cause);
        }

        private void release()
        {
            HttpClient httpClient = getHttpChannel().getHttpDestination().getHttpClient();
            ByteBufferPool bufferPool = httpClient.getByteBufferPool();
            bufferPool.release(chunkBuffer);
            chunkBuffer = null;
            contentBuffer = null;
        }
    }
}