package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.http;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpChannel;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClientTransport;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpConversation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpDestination;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpExchange;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpProxy;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpUpgrader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.IConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.SendFailure;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.AbstractConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Attachable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Promise;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.Sweeper;

public class HttpConnectionOverHTTP extends AbstractConnection implements IConnection, com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection.UpgradeFrom, Sweeper.Sweepable, Attachable
{
    private static final Logger LOG = LoggerFactory.getLogger(HttpConnectionOverHTTP.class);
   
    private final AtomicBoolean closed = new AtomicBoolean();
    private final AtomicInteger sweeps = new AtomicInteger();
    private final Promise<Connection> promise;
    private final Delegate delegate;
    private final HttpChannelOverHTTP channel;
    private final LongAdder bytesIn = new LongAdder();
    private final LongAdder bytesOut = new LongAdder();
    private long idleTimeout;

    public HttpConnectionOverHTTP(EndPoint endPoint, Map<String, Object> context)
    {
        this(endPoint, destinationFrom(context), promiseFrom(context));
    }

    private static HttpDestination destinationFrom(Map<String, Object> context)
    {
        return (HttpDestination)context.get(HttpClientTransport.HTTP_DESTINATION_CONTEXT_KEY);
    }

    @SuppressWarnings("unchecked")
    private static Promise<Connection> promiseFrom(Map<String, Object> context)
    {
        return (Promise<Connection>)context.get(HttpClientTransport.HTTP_CONNECTION_PROMISE_CONTEXT_KEY);
    }

    public HttpConnectionOverHTTP(EndPoint endPoint, HttpDestination destination, Promise<Connection> promise)
    {
        super(endPoint, destination.getHttpClient().getExecutor());
        this.promise = promise;
        this.delegate = new Delegate(destination);
        this.channel = newHttpChannel();
    }

    protected HttpChannelOverHTTP newHttpChannel()
    {
        return new HttpChannelOverHTTP(this);
    }

    public HttpChannelOverHTTP getHttpChannel()
    {
        return channel;
    }

    public HttpDestination getHttpDestination()
    {
        return delegate.getHttpDestination();
    }

    @Override
    public long getBytesIn()
    {
        return bytesIn.longValue();
    }

    protected void addBytesIn(long bytesIn)
    {
        this.bytesIn.add(bytesIn);
    }

    @Override
    public long getBytesOut()
    {
        return bytesOut.longValue();
    }

    protected void addBytesOut(long bytesOut)
    {
        this.bytesOut.add(bytesOut);
    }

    @Override
    public long getMessagesIn()
    {
        return getHttpChannel().getMessagesIn();
    }

    @Override
    public long getMessagesOut()
    {
        return getHttpChannel().getMessagesOut();
    }

    @Override
    public void send(Request request, Response.CompleteListener listener)
    {
        delegate.send(request, listener);
    }

    @Override
    public SendFailure send(HttpExchange exchange)
    {
        return delegate.send(exchange);
    }

    @Override
    public void onOpen()
    {
        super.onOpen();
        fillInterested();
        promise.succeeded(this);
    }

    @Override
    public boolean isClosed()
    {
        return closed.get();
    }

    @Override
    public void setAttachment(Object obj)
    {
        delegate.setAttachment(obj);
    }

    @Override
    public Object getAttachment()
    {
        return delegate.getAttachment();
    }

    @Override
    public boolean onIdleExpired()
    {
        long idleTimeout = getEndPoint().getIdleTimeout();
        boolean close = onIdleTimeout(idleTimeout);
        if (close)
            close(new TimeoutException("Idle timeout " + idleTimeout + " ms"));
        return false;
    }

    protected boolean onIdleTimeout(long idleTimeout)
    {
        TimeoutException failure = new TimeoutException("Idle timeout " + idleTimeout + " ms");
        return delegate.onIdleTimeout(idleTimeout, failure);
    }

    @Override
    public void onFillable()
    {
        channel.receive();
    }

    @Override
    public ByteBuffer onUpgradeFrom()
    {
        HttpReceiverOverHTTP receiver = channel.getHttpReceiver();
        return receiver.onUpgradeFrom();
    }

    public void release()
    {
        // Restore idle timeout
        getEndPoint().setIdleTimeout(idleTimeout);
        getHttpDestination().release(this);
    }

    @Override
    public void close()
    {
        close(new AsynchronousCloseException());
    }

    protected void close(Throwable failure)
    {
        if (closed.compareAndSet(false, true))
        {
            getHttpDestination().remove(this);
            abort(failure);
            channel.destroy();
            getEndPoint().shutdownOutput();
            if (LOG.isDebugEnabled())
                LOG.debug("Shutdown {}", this);
            getEndPoint().close();
            if (LOG.isDebugEnabled())
                LOG.debug("Closed {}", this);
        }
    }

    protected boolean abort(Throwable failure)
    {
        HttpExchange exchange = channel.getHttpExchange();
        return exchange != null && exchange.getRequest().abort(failure);
    }

    @Override
    public boolean sweep()
    {
        if (!closed.get())
            return false;
        if (sweeps.incrementAndGet() < 4)
            return false;
        return true;
    }

    public void remove()
    {
        getHttpDestination().remove(this);
    }

    @Override
    public String toConnectionString()
    {
        return String.format("%s@%x(l:%s <-> r:%s,closed=%b)=>%s",
            getClass().getSimpleName(),
            hashCode(),
            getEndPoint().getLocalSocketAddress(),
            getEndPoint().getRemoteSocketAddress(),
            closed.get(),
            channel);
    }

    private class Delegate extends HttpConnection
    {
        private Delegate(HttpDestination destination)
        {
            super(destination);
        }

        @Override
        protected Iterator<HttpChannel> getHttpChannels()
        {
            return Collections.<HttpChannel>singleton(channel).iterator();
        }

        @Override
        public SendFailure send(HttpExchange exchange)
        {
            HttpRequest request = exchange.getRequest();
            normalizeRequest(request);

            // Save the old idle timeout to restore it.
            EndPoint endPoint = getEndPoint();
            idleTimeout = endPoint.getIdleTimeout();
            long requestIdleTimeout = request.getIdleTimeout();
            if (requestIdleTimeout >= 0)
                endPoint.setIdleTimeout(requestIdleTimeout);

            // One channel per connection, just delegate the send.
            return send(channel, exchange);
        }

        @Override
        protected void normalizeRequest(HttpRequest request)
        {
            super.normalizeRequest(request);

            if (request instanceof HttpProxy.TunnelRequest)
            {
                long connectTimeout = getHttpClient().getConnectTimeout();
                request.timeout(connectTimeout, TimeUnit.MILLISECONDS)
                        .idleTimeout(2 * connectTimeout, TimeUnit.MILLISECONDS);
            }

            HttpConversation conversation = request.getConversation();
            HttpUpgrader upgrader = (HttpUpgrader)conversation.getAttribute(HttpUpgrader.class.getName());
            if (upgrader == null)
            {
                if (request instanceof HttpUpgrader.Factory)
                {
                    upgrader = ((HttpUpgrader.Factory)request).newHttpUpgrader(HttpVersion.HTTP_1_1);
                    conversation.setAttribute(HttpUpgrader.class.getName(), upgrader);
                    upgrader.prepare(request);
                }
                else
                {
                    String protocol = request.getHeaders().get(HttpHeader.UPGRADE);
                    if (protocol != null)
                    {
                        upgrader = new ProtocolHttpUpgrader(getHttpDestination(), protocol);
                        conversation.setAttribute(HttpUpgrader.class.getName(), upgrader);
                        upgrader.prepare(request);
                    }
                }
            }
        }

        @Override
        public void close()
        {
            HttpConnectionOverHTTP.this.close();
            destroy();
        }

        @Override
        public boolean isClosed()
        {
            return HttpConnectionOverHTTP.this.isClosed();
        }

        @Override
        public String toString()
        {
            return HttpConnectionOverHTTP.this.toString();
        }
    }
}