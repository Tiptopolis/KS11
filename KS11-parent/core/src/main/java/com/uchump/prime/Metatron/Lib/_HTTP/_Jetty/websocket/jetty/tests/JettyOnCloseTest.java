package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.containsString;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JettyOnCloseTest
{
    private Server server;
    private ServerConnector connector;
    private WebSocketClient client;
    private OnCloseEndpoint serverEndpoint = new OnCloseEndpoint();

    @WebSocket
    public static class OnCloseEndpoint extends EventSocket
    {
        private Consumer<Session> onClose;

        public void setOnClose(Consumer<Session> onClose)
        {
            this.onClose = onClose;
        }

        @Override
        public void onClose(int statusCode, String reason)
        {
            onClose.accept(session);
            super.onClose(statusCode, reason);
        }
    }

    @WebSocket
    public static class BlockingClientEndpoint extends EventSocket
    {
        private CountDownLatch blockInClose = new CountDownLatch(1);

        public void unBlockClose()
        {
            blockInClose.countDown();
        }

        @Override
        public void onClose(int statusCode, String reason)
        {
            try
            {
                blockInClose.await();
                super.onClose(statusCode, reason);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeEach
    public void start() throws Exception
    {
        server = new Server();
        connector = new ServerConnector(server);
        connector.setPort(0);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);

        JettyWebSocketServletContainerInitializer.configure(contextHandler, ((servletContext, container) ->
            container.addMapping("/", (req, resp) -> serverEndpoint)));

        client = new WebSocketClient();
        server.start();
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        client.stop();
        server.stop();
    }

    @Test
    public void changeStatusCodeInOnClose() throws Exception
    {
        EventSocket clientEndpoint = new EventSocket();
        URI uri = new URI("ws://localhost:" + connector.getLocalPort() + "/");
        client.connect(clientEndpoint, uri).get(5, TimeUnit.SECONDS);

        assertTrue(serverEndpoint.openLatch.await(5, TimeUnit.SECONDS));
        serverEndpoint.setOnClose((session) -> session.close(StatusCode.SERVICE_RESTART, "custom close reason"));

        clientEndpoint.session.close();
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.closeCode, is(StatusCode.SERVICE_RESTART));
        assertThat(clientEndpoint.closeReason, is("custom close reason"));
    }

    @Test
    public void secondCloseFromOnCloseFails() throws Exception
    {
        EventSocket clientEndpoint = new EventSocket();
        URI uri = new URI("ws://localhost:" + connector.getLocalPort() + "/");
        client.connect(clientEndpoint, uri).get(5, TimeUnit.SECONDS);

        assertTrue(serverEndpoint.openLatch.await(5, TimeUnit.SECONDS));
        serverEndpoint.setOnClose(Session::close);

        serverEndpoint.session.close(StatusCode.NORMAL, "first close");
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.closeCode, is(StatusCode.NORMAL));
        assertThat(clientEndpoint.closeReason, is("first close"));
    }

    @Test
    public void abnormalStatusDoesNotChange() throws Exception
    {
        BlockingClientEndpoint clientEndpoint = new BlockingClientEndpoint();
        URI uri = new URI("ws://localhost:" + connector.getLocalPort() + "/");
        client.connect(clientEndpoint, uri).get(5, TimeUnit.SECONDS);

        assertTrue(serverEndpoint.openLatch.await(5, TimeUnit.SECONDS));
        serverEndpoint.setOnClose((session) ->
        {
            session.close(StatusCode.SERVER_ERROR, "abnormal close 2");
            clientEndpoint.unBlockClose();
        });

        serverEndpoint.session.close(StatusCode.PROTOCOL, "abnormal close 1");
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.closeCode, is(StatusCode.PROTOCOL));
        assertThat(clientEndpoint.closeReason, is("abnormal close 1"));
    }

    @Test
    public void onErrorOccurringAfterOnClose() throws Exception
    {
        EventSocket clientEndpoint = new EventSocket();
        URI uri = new URI("ws://localhost:" + connector.getLocalPort() + "/");
        client.connect(clientEndpoint, uri).get(5, TimeUnit.SECONDS);

        assertTrue(serverEndpoint.openLatch.await(5, TimeUnit.SECONDS));
        serverEndpoint.setOnClose((session) ->
        {
            throw new RuntimeException("trigger onError from onClose");
        });

        clientEndpoint.session.close();
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
        assertThat(clientEndpoint.closeCode, is(StatusCode.SERVER_ERROR));
        assertThat(clientEndpoint.closeReason, containsString("trigger onError from onClose"));

        assertTrue(serverEndpoint.errorLatch.await(5, TimeUnit.SECONDS));
        assertThat(serverEndpoint.error, instanceOf(RuntimeException.class));
        assertThat(serverEndpoint.error.getMessage(), containsString("trigger onError from onClose"));
    }
}