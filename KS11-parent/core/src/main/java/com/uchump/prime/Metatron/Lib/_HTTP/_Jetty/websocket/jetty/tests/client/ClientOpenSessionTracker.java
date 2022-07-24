package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.client;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketSessionListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;

public class ClientOpenSessionTracker implements Connection.Listener, WebSocketSessionListener
{
    private final CountDownLatch closeSessionLatch;
    private final CountDownLatch closeConnectionLatch;

    public ClientOpenSessionTracker(int expectedSessions)
    {
        this.closeSessionLatch = new CountDownLatch(expectedSessions);
        this.closeConnectionLatch = new CountDownLatch(expectedSessions);
    }

    public void addTo(WebSocketClient client)
    {
        client.addSessionListener(this);
        client.addBean(this);
    }

    public void assertClosedProperly(WebSocketClient client) throws InterruptedException
    {
        assertTrue(closeConnectionLatch.await(5, SECONDS), "All Jetty Connections should have been closed");
        assertTrue(closeSessionLatch.await(5, SECONDS), "All WebSocket Sessions should have been closed");
        assertTrue(client.getOpenSessions().isEmpty(), "Client OpenSessions MUST be empty");
    }

    @Override
    public void onOpened(Connection connection)
    {
    }

    @Override
    public void onClosed(Connection connection)
    {
        this.closeConnectionLatch.countDown();
    }

    @Override
    public void onWebSocketSessionClosed(Session session)
    {
        this.closeSessionLatch.countDown();
    }
}