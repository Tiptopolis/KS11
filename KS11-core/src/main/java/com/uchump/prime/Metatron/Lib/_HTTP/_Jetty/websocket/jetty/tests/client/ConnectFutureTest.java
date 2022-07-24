package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.client;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketSessionListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions.UpgradeException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions.WebSocketException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.util.WSURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.JettyUpgradeListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServerContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.CloseTrackingEndpoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EchoSocket;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matchers;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectFutureTest
{
    private Server server;
    private WebSocketClient client;

    public void start(Consumer<JettyWebSocketServerContainer> configuration) throws Exception
    {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);

        JettyWebSocketServletContainerInitializer.configure(contextHandler, (context, container) ->
            configuration.accept(container));
        server.start();

        client = new WebSocketClient();
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        if (client != null)
            client.stop();
        if (server != null)
            server.stop();
    }

    @Test
    public void testAbortDuringCreator() throws Exception
    {
        CountDownLatch enteredCreator = new CountDownLatch(1);
        CountDownLatch exitCreator = new CountDownLatch(1);
        start(c ->
        {
            c.addMapping("/", (req, res) ->
            {
                try
                {
                    enteredCreator.countDown();
                    exitCreator.await();
                    return new EchoSocket();
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            });
        });

        CloseTrackingEndpoint clientSocket = new CloseTrackingEndpoint();
        Future<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()));

        // Cancel the future once we have entered the servers WebSocketCreator (after upgrade request is sent).
        assertTrue(enteredCreator.await(5, TimeUnit.SECONDS));
        assertTrue(connect.cancel(true));
        assertThrows(CancellationException.class, () -> connect.get(5, TimeUnit.SECONDS));
        exitCreator.countDown();
        assertFalse(clientSocket.openLatch.await(1, TimeUnit.SECONDS));

        Throwable error = clientSocket.error.get();
        assertThat(error, instanceOf(UpgradeException.class));
        Throwable cause = error.getCause();
        assertThat(cause, instanceOf(com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.UpgradeException.class));
        assertThat(cause.getCause(), instanceOf(CancellationException.class));
    }

    @Test
    public void testAbortSessionOnCreated() throws Exception
    {
        start(c -> c.addMapping("/", EchoSocket.class));

        CountDownLatch enteredListener = new CountDownLatch(1);
        CountDownLatch exitListener = new CountDownLatch(1);
        client.addSessionListener(new WebSocketSessionListener()
        {
            @Override
            public void onWebSocketSessionCreated(Session session)
            {
                try
                {
                    enteredListener.countDown();
                    exitListener.await();
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            }
        });

        CloseTrackingEndpoint clientSocket = new CloseTrackingEndpoint();
        Future<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()));

        // Abort when session is created, this is during the connection upgrade.
        assertTrue(enteredListener.await(5, TimeUnit.SECONDS));
        assertTrue(connect.cancel(true));
        assertThrows(CancellationException.class, () -> connect.get(5, TimeUnit.SECONDS));
        exitListener.countDown();
        assertTrue(clientSocket.openLatch.await(5, TimeUnit.SECONDS));
        assertTrue(clientSocket.errorLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientSocket.error.get(), instanceOf(CancellationException.class));
    }

    @Test
    public void testAbortInHandshakeResponse() throws Exception
    {
        start(c -> c.addMapping("/", EchoSocket.class));

        CountDownLatch enteredListener = new CountDownLatch(1);
        CountDownLatch exitListener = new CountDownLatch(1);
        JettyUpgradeListener upgradeListener = new JettyUpgradeListener()
        {
            @Override
            public void onHandshakeResponse(HttpRequest request, HttpResponse response)
            {
                try
                {
                    enteredListener.countDown();
                    exitListener.await();
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            }
        };

        CloseTrackingEndpoint clientSocket = new CloseTrackingEndpoint();
        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        Future<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()), upgradeRequest, upgradeListener);

        // Abort after after handshake response, this is during the connection upgrade.
        assertTrue(enteredListener.await(5, TimeUnit.SECONDS));
        assertTrue(connect.cancel(true));
        assertThrows(CancellationException.class, () -> connect.get(5, TimeUnit.SECONDS));
        exitListener.countDown();
        assertTrue(clientSocket.openLatch.await(5, TimeUnit.SECONDS));
        assertTrue(clientSocket.errorLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientSocket.error.get(), instanceOf(CancellationException.class));
    }

    @Test
    public void testAbortOnOpened() throws Exception
    {
        start(c -> c.addMapping("/", EchoSocket.class));

        CountDownLatch exitOnOpen = new CountDownLatch(1);
        CloseTrackingEndpoint clientSocket = new CloseTrackingEndpoint()
        {
            @Override
            public void onWebSocketConnect(Session session)
            {
                try
                {
                    super.onWebSocketConnect(session);
                    exitOnOpen.await();
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            }
        };

        // Abort during the call to onOpened. This is after the connection upgrade, but before future completion.
        Future<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()));
        assertTrue(clientSocket.openLatch.await(5, TimeUnit.SECONDS));
        assertTrue(connect.cancel(true));
        exitOnOpen.countDown();

        // We got an error on the WebSocket endpoint and an error from the future.
        assertTrue(clientSocket.errorLatch.await(5, TimeUnit.SECONDS));
        assertThrows(CancellationException.class, () -> connect.get(5, TimeUnit.SECONDS));
    }

    @Test
    public void testAbortAfterCompletion() throws Exception
    {
        start(c -> c.addMapping("/", EchoSocket.class));

        CloseTrackingEndpoint clientSocket = new CloseTrackingEndpoint();
        Future<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()));
        Session session = connect.get(5, TimeUnit.SECONDS);

        // If we can send and receive messages the future has been completed.
        assertTrue(clientSocket.openLatch.await(5, TimeUnit.SECONDS));
        clientSocket.getSession().getRemote().sendString("hello");
        assertThat(clientSocket.messageQueue.poll(5, TimeUnit.SECONDS), Matchers.is("hello"));

        // After it has been completed we should not get any errors from cancelling it.
        assertFalse(connect.cancel(true));
        assertThat(connect.get(5, TimeUnit.SECONDS), instanceOf(Session.class));
        assertFalse(clientSocket.closeLatch.await(1, TimeUnit.SECONDS));
        assertNull(clientSocket.error.get());

        // Close the session properly.
        session.close();
        assertTrue(clientSocket.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientSocket.closeCode, is(StatusCode.NORMAL));
    }

    @Test
    public void testFutureTimeout() throws Exception
    {
        CountDownLatch exitCreator = new CountDownLatch(1);
        start(c ->
        {
            c.addMapping("/", (req, res) ->
            {
                try
                {
                    exitCreator.await();
                    return new EchoSocket();
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            });
        });

        CloseTrackingEndpoint clientSocket = new CloseTrackingEndpoint();
        Future<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()));
        assertThrows(TimeoutException.class, () -> connect.get(1, TimeUnit.SECONDS));
        exitCreator.countDown();
        Session session = connect.get(5, TimeUnit.SECONDS);

        // Close the session properly.
        session.close();
        assertTrue(clientSocket.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientSocket.closeCode, is(StatusCode.NORMAL));
    }

    @Test
    public void testAbortWithExceptionBeforeUpgrade() throws Exception
    {
        CountDownLatch exitCreator = new CountDownLatch(1);
        start(c ->
        {
            c.addMapping("/", (req, res) ->
            {
                try
                {
                    exitCreator.await();
                    return new EchoSocket();
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            });
        });

        // Complete the CompletableFuture with an exception the during the call to onOpened.
        CloseTrackingEndpoint clientSocket = new CloseTrackingEndpoint();
        CompletableFuture<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()));
        assertTrue(connect.completeExceptionally(new WebSocketException("custom exception")));
        exitCreator.countDown();

        // Exception from the future is correct.
        ExecutionException futureError = assertThrows(ExecutionException.class, () -> connect.get(5, TimeUnit.SECONDS));
        Throwable futureCause = futureError.getCause();
        assertThat(futureCause, instanceOf(WebSocketException.class));
        assertThat(futureCause.getMessage(), is("custom exception"));

        // Exception from the endpoint is correct.
        assertTrue(clientSocket.errorLatch.await(5, TimeUnit.SECONDS));
        Throwable upgradeException = clientSocket.error.get();
        assertThat(upgradeException, instanceOf(UpgradeException.class));
        Throwable coreUpgradeException = upgradeException.getCause();
        assertThat(coreUpgradeException, instanceOf(com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.UpgradeException.class));
        Throwable cause = coreUpgradeException.getCause();
        assertThat(cause, instanceOf(WebSocketException.class));
        assertThat(cause.getMessage(), is("custom exception"));
    }

    @Test
    public void testAbortWithExceptionAfterUpgrade() throws Exception
    {
        start(c -> c.addMapping("/", EchoSocket.class));
        CountDownLatch exitOnOpen = new CountDownLatch(1);
        CloseTrackingEndpoint clientSocket = new CloseTrackingEndpoint()
        {
            @Override
            public void onWebSocketConnect(Session session)
            {
                try
                {
                    super.onWebSocketConnect(session);
                    exitOnOpen.await();
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            }
        };

        // Complete the CompletableFuture with an exception the during the call to onOpened.
        CompletableFuture<Session> connect = client.connect(clientSocket, WSURI.toWebsocket(server.getURI()));
        assertTrue(clientSocket.openLatch.await(5, TimeUnit.SECONDS));
        assertTrue(connect.completeExceptionally(new WebSocketException("custom exception")));
        exitOnOpen.countDown();

        // Exception from the future is correct.
        ExecutionException futureError = assertThrows(ExecutionException.class, () -> connect.get(5, TimeUnit.SECONDS));
        Throwable cause = futureError.getCause();
        assertThat(cause, instanceOf(WebSocketException.class));
        assertThat(cause.getMessage(), is("custom exception"));

        // Exception from the endpoint is correct.
        assertTrue(clientSocket.errorLatch.await(5, TimeUnit.SECONDS));
        Throwable endpointError = clientSocket.error.get();
        assertThat(endpointError, instanceOf(WebSocketException.class));
        assertThat(endpointError.getMessage(), is("custom exception"));
    }
}