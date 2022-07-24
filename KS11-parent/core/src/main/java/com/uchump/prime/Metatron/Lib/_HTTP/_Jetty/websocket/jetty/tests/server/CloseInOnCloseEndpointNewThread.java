package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import java.util.concurrent.CountDownLatch;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;


public class CloseInOnCloseEndpointNewThread extends AbstractCloseEndpoint
{
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        try
        {
            CountDownLatch complete = new CountDownLatch(1);
            new Thread(() ->
            {
                getSession().close(StatusCode.SERVER_ERROR, "this should be a noop");
                complete.countDown();
            }).start();
            complete.await();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        super.onWebSocketClose(statusCode, reason);
    }
}