package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.http;


import java.io.EOFException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.LongAdder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClientTransport;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpExchange;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpReceiver;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponseException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.BadMessageException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpParser;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.RetainableByteBuffer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.RetainableByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;

public class HttpReceiverOverHTTP extends HttpReceiver implements HttpParser.ResponseHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(HttpReceiverOverHTTP.class);

    private final LongAdder inMessages = new LongAdder();
    private final HttpParser parser;
    private final RetainableByteBufferPool retainableByteBufferPool;
    private RetainableByteBuffer networkBuffer;
    private boolean shutdown;
    private boolean complete;
    private boolean unsolicited;
    private String method;
    private int status;

    public HttpReceiverOverHTTP(HttpChannelOverHTTP channel)
    {
        super(channel);
        HttpClient httpClient = channel.getHttpDestination().getHttpClient();
        parser = new HttpParser(this, -1, httpClient.getHttpCompliance());
        HttpClientTransport transport = httpClient.getTransport();
        if (transport instanceof HttpClientTransportOverHTTP)
        {
            HttpClientTransportOverHTTP httpTransport = (HttpClientTransportOverHTTP)transport;
            parser.setHeaderCacheSize(httpTransport.getHeaderCacheSize());
            parser.setHeaderCacheCaseSensitive(httpTransport.isHeaderCacheCaseSensitive());
        }

        this.retainableByteBufferPool = httpClient.getByteBufferPool().asRetainableByteBufferPool();
    }

    @Override
    public HttpChannelOverHTTP getHttpChannel()
    {
        return (HttpChannelOverHTTP)super.getHttpChannel();
    }

    private HttpConnectionOverHTTP getHttpConnection()
    {
        return getHttpChannel().getHttpConnection();
    }

    protected ByteBuffer getResponseBuffer()
    {
        return networkBuffer == null ? null : networkBuffer.getBuffer();
    }

    @Override
    public void receive()
    {
        if (networkBuffer == null)
            acquireNetworkBuffer();
        process();
    }

    private void acquireNetworkBuffer()
    {
        networkBuffer = newNetworkBuffer();
        if (LOG.isDebugEnabled())
            LOG.debug("Acquired {}", networkBuffer);
    }

    private void reacquireNetworkBuffer()
    {
        RetainableByteBuffer currentBuffer = networkBuffer;
        if (currentBuffer == null)
            throw new IllegalStateException();
        if (currentBuffer.hasRemaining())
            throw new IllegalStateException();

        currentBuffer.release();
        networkBuffer = newNetworkBuffer();
        if (LOG.isDebugEnabled())
            LOG.debug("Reacquired {} <- {}", currentBuffer, networkBuffer);
    }

    private RetainableByteBuffer newNetworkBuffer()
    {
        HttpClient client = getHttpDestination().getHttpClient();
        boolean direct = client.isUseInputDirectByteBuffers();
        return retainableByteBufferPool.acquire(client.getResponseBufferSize(), direct);
    }

    private void releaseNetworkBuffer()
    {
        if (networkBuffer == null)
            return;
        networkBuffer.release();
        if (LOG.isDebugEnabled())
            LOG.debug("Released {}", networkBuffer);
        networkBuffer = null;
    }

    protected ByteBuffer onUpgradeFrom()
    {
        RetainableByteBuffer networkBuffer = this.networkBuffer;
        if (networkBuffer == null)
            return null;

        ByteBuffer upgradeBuffer = null;
        if (networkBuffer.hasRemaining())
        {
            HttpClient client = getHttpDestination().getHttpClient();
            upgradeBuffer = BufferUtil.allocate(networkBuffer.remaining(), client.isUseInputDirectByteBuffers());
            BufferUtil.clearToFill(upgradeBuffer);
            BufferUtil.put(networkBuffer.getBuffer(), upgradeBuffer);
            BufferUtil.flipToFlush(upgradeBuffer, 0);
        }
        releaseNetworkBuffer();
        return upgradeBuffer;
    }

    private void process()
    {
        HttpConnectionOverHTTP connection = getHttpConnection();
        EndPoint endPoint = connection.getEndPoint();
        try
        {
            while (true)
            {
                // Always parse even empty buffers to advance the parser.
                if (parse())
                {
                    // Return immediately, as this thread may be in a race
                    // with e.g. another thread demanding more content.
                    return;
                }

                // Connection may be closed in a parser callback.
                if (connection.isClosed())
                {
                    if (LOG.isDebugEnabled())
                        LOG.debug("Closed {}", connection);
                    releaseNetworkBuffer();
                    return;
                }

                if (networkBuffer.isRetained())
                    reacquireNetworkBuffer();

                // The networkBuffer may have been reacquired.
                int read = endPoint.fill(networkBuffer.getBuffer());
                if (LOG.isDebugEnabled())
                    LOG.debug("Read {} bytes in {} from {}", read, networkBuffer, endPoint);

                if (read > 0)
                {
                    connection.addBytesIn(read);
                }
                else if (read == 0)
                {
                    assert networkBuffer.isEmpty();
                    releaseNetworkBuffer();
                    fillInterested();
                    return;
                }
                else
                {
                    releaseNetworkBuffer();
                    shutdown();
                    return;
                }
            }
        }
        catch (Throwable x)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Error processing {}", endPoint, x);
            releaseNetworkBuffer();
            failAndClose(x);
        }
    }

    /**
     * Parses an HTTP response in the receivers buffer.
     *
     * @return true to indicate that parsing should be interrupted (and will be resumed by another thread).
     */
    private boolean parse()
    {
        while (true)
        {
            boolean handle = parser.parseNext(networkBuffer.getBuffer());
            boolean failed = isFailed();
            if (LOG.isDebugEnabled())
                LOG.debug("Parse result={}, failed={}", handle, failed);
            // When failed, it's safe to close the parser because there
            // will be no races with other threads demanding more content.
            if (failed)
                parser.close();
            if (handle)
                return !failed;

            boolean complete = this.complete;
            this.complete = false;
            if (LOG.isDebugEnabled())
                LOG.debug("Parse complete={}, {} {}", complete, networkBuffer, parser);

            if (complete)
            {
                int status = this.status;
                this.status = 0;
                // Connection upgrade due to 101, bail out.
                if (status == HttpStatus.SWITCHING_PROTOCOLS_101)
                    return true;
                // Connection upgrade due to CONNECT + 200, bail out.
                String method = this.method;
                this.method = null;
                if (getHttpChannel().isTunnel(method, status))
                    return true;

                if (networkBuffer.isEmpty())
                    return false;

                if (!HttpStatus.isInformational(status))
                {
                    if (LOG.isDebugEnabled())
                        LOG.debug("Discarding unexpected content after response {}: {}", status, networkBuffer);
                    networkBuffer.clear();
                }
                return false;
            }

            if (networkBuffer.isEmpty())
                return false;
        }
    }

    protected void fillInterested()
    {
        getHttpConnection().fillInterested();
    }

    private void shutdown()
    {
        // Mark this receiver as shutdown, so that we can
        // close the connection when the exchange terminates.
        // We cannot close the connection from here because
        // the request may still be in process.
        shutdown = true;

        // Shutting down the parser may invoke messageComplete() or earlyEOF().
        // In case of content delimited by EOF, without a Connection: close
        // header, the connection will be closed at exchange termination
        // thanks to the flag we have set above.
        parser.atEOF();
        parser.parseNext(BufferUtil.EMPTY_BUFFER);
    }

    protected boolean isShutdown()
    {
        return shutdown;
    }

    @Override
    public void startResponse(HttpVersion version, int status, String reason)
    {
        HttpExchange exchange = getHttpExchange();
        unsolicited = exchange == null;
        if (exchange == null)
            return;

        this.method = exchange.getRequest().getMethod();
        this.status = status;
        parser.setHeadResponse(HttpMethod.HEAD.is(method) || getHttpChannel().isTunnel(method, status));
        exchange.getResponse().version(version).status(status).reason(reason);

        responseBegin(exchange);
    }

    @Override
    public void parsedHeader(HttpField field)
    {
        HttpExchange exchange = getHttpExchange();
        unsolicited |= exchange == null;
        if (unsolicited)
            return;

        responseHeader(exchange, field);
    }

    @Override
    public boolean headerComplete()
    {
        HttpExchange exchange = getHttpExchange();
        unsolicited |= exchange == null;
        if (unsolicited)
            return false;

        // Store the EndPoint is case of upgrades, tunnels, etc.
        exchange.getRequest().getConversation().setAttribute(EndPoint.class.getName(), getHttpConnection().getEndPoint());
        return !responseHeaders(exchange);
    }

    @Override
    public boolean content(ByteBuffer buffer)
    {
        HttpExchange exchange = getHttpExchange();
        unsolicited |= exchange == null;
        if (unsolicited)
            return false;

        RetainableByteBuffer networkBuffer = this.networkBuffer;
        networkBuffer.retain();
        return !responseContent(exchange, buffer, Callback.from(networkBuffer::release, failure ->
        {
            networkBuffer.release();
            failAndClose(failure);
        }));
    }

    @Override
    public boolean contentComplete()
    {
        return false;
    }

    @Override
    public void parsedTrailer(HttpField trailer)
    {
        HttpExchange exchange = getHttpExchange();
        unsolicited |= exchange == null;
        if (unsolicited)
            return;

        exchange.getResponse().trailer(trailer);
    }

    @Override
    public boolean messageComplete()
    {
        HttpExchange exchange = getHttpExchange();
        if (exchange == null || unsolicited)
        {
            // We received an unsolicited response from the server.
            getHttpConnection().close();
            return false;
        }

        int status = exchange.getResponse().getStatus();
        if (!HttpStatus.isInterim(status))
        {
            inMessages.increment();
            complete = true;
        }

        boolean stopParsing = !responseSuccess(exchange);
        if (status == HttpStatus.SWITCHING_PROTOCOLS_101)
            stopParsing = true;
        return stopParsing;
    }

    @Override
    public void earlyEOF()
    {
        HttpExchange exchange = getHttpExchange();
        HttpConnectionOverHTTP connection = getHttpConnection();
        if (exchange == null || unsolicited)
            connection.close();
        else
            failAndClose(new EOFException(String.valueOf(connection)));
    }

    @Override
    public void badMessage(BadMessageException failure)
    {
        HttpExchange exchange = getHttpExchange();
        if (exchange == null || unsolicited)
        {
            getHttpConnection().close();
        }
        else
        {
            HttpResponse response = exchange.getResponse();
            response.status(failure.getCode()).reason(failure.getReason());
            failAndClose(new HttpResponseException("HTTP protocol violation: bad response on " + getHttpConnection(), response, failure));
        }
    }

    @Override
    protected void reset()
    {
        super.reset();
        parser.reset();
    }

    private void failAndClose(Throwable failure)
    {
        if (responseFailure(failure))
            getHttpConnection().close(failure);
    }

    long getMessagesIn()
    {
        return inMessages.longValue();
    }

    @Override
    public String toString()
    {
        return String.format("%s[%s]", super.toString(), parser);
    }
}