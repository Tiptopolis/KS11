package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.JettyUpgradeListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JettyWebSocketExtensionConfigTest
{
    private Server server;
    private ServerConnector connector;
    private WebSocketClient client;

    @BeforeEach
    public void start() throws Exception
    {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);

        JettyWebSocketServletContainerInitializer.configure(contextHandler,
            (context, container) -> container.addMapping("/", (req, resp) ->
            {
                assertEquals(req.getExtensions().stream().filter(e -> e.getName().equals("permessage-deflate")).count(), 1);
                assertEquals(resp.getExtensions().stream().filter(e -> e.getName().equals("permessage-deflate")).count(), 1);

                ExtensionConfig nonRequestedExtension = ExtensionConfig.parse("identity");
                assertNotNull(nonRequestedExtension);

                assertThrows(IllegalArgumentException.class,
                    () -> resp.setExtensions(List.of(nonRequestedExtension)),
                    "should not allow extensions not requested");

                // Check identity extension was not added because it was not requested
                assertEquals(resp.getExtensions().stream().filter(config -> config.getName().equals("identity")).count(), 0);
                assertEquals(resp.getExtensions().stream().filter(e -> e.getName().equals("permessage-deflate")).count(), 1);

                return new EchoSocket();
            }));

        server.start();

        client = new WebSocketClient();
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        client.stop();
        server.stop();
    }

    @Test
    public void testJettyExtensionConfig() throws Exception
    {
        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/filterPath");
        EventSocket socket = new EventSocket();

        ClientUpgradeRequest request = new ClientUpgradeRequest();
        request.addExtensions(ExtensionConfig.parse("permessage-deflate"));

        CountDownLatch correctResponseExtensions = new CountDownLatch(1);
        JettyUpgradeListener listener = new JettyUpgradeListener()
        {
            @Override
            public void onHandshakeResponse(HttpRequest request, HttpResponse response)
            {

                String extensions = response.getHeaders().get(HttpHeader.SEC_WEBSOCKET_EXTENSIONS);
                if ("permessage-deflate".equals(extensions))
                    correctResponseExtensions.countDown();
                else
                    throw new IllegalStateException("Unexpected Negotiated Extensions: " + extensions);
            }
        };

        CompletableFuture<Session> connect = client.connect(socket, uri, request, listener);
        try (Session session = connect.get(5, TimeUnit.SECONDS))
        {
            session.getRemote().sendString("hello world");
        }
        assertTrue(socket.closeLatch.await(5, TimeUnit.SECONDS));
        assertTrue(correctResponseExtensions.await(5, TimeUnit.SECONDS));

        String msg = socket.textMessages.poll();
        assertThat(msg, is("hello world"));
    }
}