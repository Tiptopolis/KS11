package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.http;

import java.util.concurrent.atomic.LongAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpChannel;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpExchange;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Result;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpFields;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeaderValue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.MetaData;

public class HttpChannelOverHTTP extends HttpChannel
{
    private static final Logger LOG = LoggerFactory.getLogger(HttpChannelOverHTTP.class);

    private final HttpConnectionOverHTTP connection;
    private final HttpSenderOverHTTP sender;
    private final HttpReceiverOverHTTP receiver;
    private final LongAdder outMessages = new LongAdder();

    public HttpChannelOverHTTP(HttpConnectionOverHTTP connection)
    {
        super(connection.getHttpDestination());
        this.connection = connection;
        this.sender = newHttpSender();
        this.receiver = newHttpReceiver();
    }

    protected HttpSenderOverHTTP newHttpSender()
    {
        return new HttpSenderOverHTTP(this);
    }

    protected HttpReceiverOverHTTP newHttpReceiver()
    {
        return new HttpReceiverOverHTTP(this);
    }

    @Override
    protected HttpSenderOverHTTP getHttpSender()
    {
        return sender;
    }

    @Override
    protected HttpReceiverOverHTTP getHttpReceiver()
    {
        return receiver;
    }

    public HttpConnectionOverHTTP getHttpConnection()
    {
        return connection;
    }

    @Override
    public void send(HttpExchange exchange)
    {
        outMessages.increment();
        sender.send(exchange);
    }

    @Override
    public void release()
    {
        connection.release();
    }

    public void receive()
    {
        receiver.receive();
    }

    @Override
    public void exchangeTerminated(HttpExchange exchange, Result result)
    {
        super.exchangeTerminated(exchange, result);

        String method = exchange.getRequest().getMethod();
        Response response = result.getResponse();
        HttpFields responseHeaders = response.getHeaders();

        String closeReason = null;
        if (result.isFailed())
            closeReason = "failure";
        else if (receiver.isShutdown())
            closeReason = "server close";
        else if (sender.isShutdown() && response.getStatus() != HttpStatus.SWITCHING_PROTOCOLS_101)
            closeReason = "client close";

        if (closeReason == null)
        {
            if (response.getVersion().compareTo(HttpVersion.HTTP_1_1) < 0)
            {
                // HTTP 1.0 must close the connection unless it has
                // an explicit keep alive or it's a CONNECT method.
                boolean keepAlive = responseHeaders.contains(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE.asString());
                boolean connect = HttpMethod.CONNECT.is(method);
                if (!keepAlive && !connect)
                    closeReason = "http/1.0";
            }
            else
            {
                // HTTP 1.1 closes only if it has an explicit close.
                if (responseHeaders.contains(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.asString()))
                    closeReason = "http/1.1";
            }
        }

        if (closeReason != null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Closing, reason: {} - {}", closeReason, connection);
            connection.close();
        }
        else
        {
            int status = response.getStatus();
            if (status == HttpStatus.SWITCHING_PROTOCOLS_101 || isTunnel(method, status))
                connection.remove();
            else
                release();
        }
    }

    protected long getMessagesIn()
    {
        return receiver.getMessagesIn();
    }

    protected long getMessagesOut()
    {
        return outMessages.longValue();
    }

    boolean isTunnel(String method, int status)
    {
        return MetaData.isTunnel(method, status);
    }

    @Override
    public String toString()
    {
        return String.format("%s[send=%s,recv=%s]",
            super.toString(),
            sender,
            receiver);
    }
}