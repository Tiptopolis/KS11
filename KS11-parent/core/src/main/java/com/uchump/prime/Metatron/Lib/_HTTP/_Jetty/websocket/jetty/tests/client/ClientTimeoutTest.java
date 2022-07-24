package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.client;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions.UpgradeException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.util.WSURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EchoSocket;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EventSocket;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientTimeoutTest
{
    private Server server;
    private WebSocketClient client;
    private final CountDownLatch createEndpoint = new CountDownLatch(1);

    @BeforeEach
    public void start() throws Exception
    {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);

        JettyWebSocketServletContainerInitializer.configure(contextHandler, (context, container) ->
        {
            container.addMapping("/", (req, res) ->
            {
                try
                {
                    createEndpoint.await(5, TimeUnit.SECONDS);
                    return new EchoSocket();
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            });
        });

        server.start();

        client = new WebSocketClient();
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        createEndpoint.countDown();
        client.stop();
        server.stop();
    }

    @Test
    public void testWebSocketClientTimeout() throws Exception
    {
        EventSocket clientSocket = new EventSocket();
        long timeout = 1000;
        client.setIdleTimeout(Duration.ofMillis(timeout));
        Future<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()));

        ExecutionException executionException = assertThrows(ExecutionException.class, () -> connect.get(timeout * 2, TimeUnit.MILLISECONDS));
        assertThat(executionException.getCause(), instanceOf(UpgradeException.class));
        UpgradeException upgradeException = (UpgradeException)executionException.getCause();
        assertThat(upgradeException.getCause(), instanceOf(com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.UpgradeException.class));
        com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.UpgradeException coreUpgradeException =
            (com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.UpgradeException)upgradeException.getCause();
        assertThat(coreUpgradeException.getCause(), instanceOf(TimeoutException.class));
    }

    @Test
    public void testClientUpgradeRequestTimeout() throws Exception
    {
        EventSocket clientSocket = new EventSocket();
        long timeout = 1000;
        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        upgradeRequest.setTimeout(timeout, TimeUnit.MILLISECONDS);
        Future<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()), upgradeRequest);

        ExecutionException executionException = assertThrows(ExecutionException.class, () -> connect.get(timeout * 2, TimeUnit.MILLISECONDS));
        assertThat(executionException.getCause(), instanceOf(UpgradeException.class));
        UpgradeException upgradeException = (UpgradeException)executionException.getCause();
        assertThat(upgradeException.getCause(), instanceOf(com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.UpgradeException.class));
        com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.UpgradeException coreUpgradeException =
            (com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.UpgradeException)upgradeException.getCause();
        assertThat(coreUpgradeException.getCause(), instanceOf(TimeoutException.class));
    }
}