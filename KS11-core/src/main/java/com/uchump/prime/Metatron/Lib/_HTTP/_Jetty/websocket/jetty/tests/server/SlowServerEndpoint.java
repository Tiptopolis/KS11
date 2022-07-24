package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class SlowServerEndpoint
{
    private static final Logger LOG = LoggerFactory.getLogger(SlowServerEndpoint.class);

    @OnWebSocketMessage
    public void onMessage(Session session, String msg)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (msg.startsWith("send-slow|"))
        {
            int idx = msg.indexOf('|');
            int msgCount = Integer.parseInt(msg.substring(idx + 1));
            CompletableFuture.runAsync(() ->
            {
                for (int i = 0; i < msgCount; i++)
                {
                    try
                    {
                        session.getRemote().sendString("Hello/" + i + "/");
                        // fake some slowness
                        TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
                    }
                    catch (Throwable cause)
                    {
                        LOG.warn("failed to send text", cause);
                    }
                }
            });
        }
        else
        {
            // echo message.
            try
            {
                session.getRemote().sendString(msg);
            }
            catch (IOException ignore)
            {
                LOG.trace("IGNORED", ignore);
            }
        }
    }
}