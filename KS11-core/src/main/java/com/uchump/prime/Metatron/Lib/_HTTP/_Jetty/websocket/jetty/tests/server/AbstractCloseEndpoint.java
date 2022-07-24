package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketAdapter;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.nullValue;

public abstract class AbstractCloseEndpoint extends WebSocketAdapter
{
    public final Logger log;
    public CountDownLatch openLatch = new CountDownLatch(1);
    public CountDownLatch closeLatch = new CountDownLatch(1);
    public String closeReason = null;
    public int closeStatusCode = -1;
    public LinkedBlockingQueue<Throwable> errors = new LinkedBlockingQueue<>();

    public AbstractCloseEndpoint()
    {
        this.log = LoggerFactory.getLogger(this.getClass().getName());
    }

    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        log.debug("onWebSocketConnect({})", sess);
        openLatch.countDown();
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        log.debug("onWebSocketClose({}, {})", statusCode, reason);
        this.closeStatusCode = statusCode;
        this.closeReason = reason;
        closeLatch.countDown();
    }

    @Override
    public void onWebSocketError(Throwable cause)
    {
        log.debug("onWebSocketError({})", cause.getClass().getSimpleName());
        errors.offer(cause);
    }

    public void assertReceivedCloseEvent(int clientTimeoutMs, Matcher<Integer> statusCodeMatcher, Matcher<String> reasonMatcher)
        throws InterruptedException
    {
        assertThat("Client Close Event Occurred", closeLatch.await(clientTimeoutMs, TimeUnit.MILLISECONDS), is(true));
        assertThat("Client Close Event Status Code", closeStatusCode, statusCodeMatcher);
        if (reasonMatcher == null)
        {
            assertThat("Client Close Event Reason", closeReason, nullValue());
        }
        else
        {
            assertThat("Client Close Event Reason", closeReason, reasonMatcher);
        }
    }
}