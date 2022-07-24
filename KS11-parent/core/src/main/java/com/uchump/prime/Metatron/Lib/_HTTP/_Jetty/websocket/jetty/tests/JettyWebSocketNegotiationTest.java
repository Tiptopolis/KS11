package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpChannel;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging.StacklessLogging;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.JettyUpgradeListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServerContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.containsString;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JettyWebSocketNegotiationTest
{
    private Server server;
    private ServerConnector connector;
    private WebSocketClient client;
    private ServletContextHandler contextHandler;

    @BeforeEach
    public void start() throws Exception
    {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);
        JettyWebSocketServletContainerInitializer.configure(contextHandler, null);
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
    public void testBadRequest() throws Exception
    {
        JettyWebSocketServerContainer container = JettyWebSocketServerContainer.getContainer(contextHandler.getServletContext());
        container.addMapping("/", (req, resp) -> new EchoSocket());

        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/filterPath");
        EventSocket socket = new EventSocket();

        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        upgradeRequest.addExtensions("permessage-deflate;invalidParameter");

        CompletableFuture<Session> connect = client.connect(socket, uri, upgradeRequest);
        Throwable t = assertThrows(ExecutionException.class, () -> connect.get(5, TimeUnit.SECONDS));
        assertThat(t.getMessage(), containsString("Failed to upgrade to websocket:"));
        assertThat(t.getMessage(), containsString("400 Bad Request"));
    }

    @Test
    public void testServerError() throws Exception
    {
        JettyWebSocketServerContainer container = JettyWebSocketServerContainer.getContainer(contextHandler.getServletContext());
        container.addMapping("/", (req, resp) ->
        {
            resp.setAcceptedSubProtocol("errorSubProtocol");
            return new EchoSocket();
        });

        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/filterPath");
        EventSocket socket = new EventSocket();

        try (StacklessLogging stacklessLogging = new StacklessLogging(HttpChannel.class))
        {
            CompletableFuture<Session> connect = client.connect(socket, uri);
            Throwable t = assertThrows(ExecutionException.class, () -> connect.get(5, TimeUnit.SECONDS));
            assertThat(t.getMessage(), containsString("Failed to upgrade to websocket:"));
            assertThat(t.getMessage(), containsString("500 Server Error"));
        }
    }

    @Test
    public void testManualNegotiationInCreator() throws Exception
    {
        JettyWebSocketServerContainer container = JettyWebSocketServerContainer.getContainer(contextHandler.getServletContext());
        container.addMapping("/", (req, resp) ->
        {
            long matchedExts = req.getExtensions().stream()
                .filter(ec -> "permessage-deflate".equals(ec.getName()))
                .filter(ec -> ec.getParameters().containsKey("client_no_context_takeover"))
                .count();
            assertThat(matchedExts, is(1L));

            // Manually drop the param so it is not negotiated in the extension stack.
            resp.setHeader("Sec-WebSocket-Extensions", "permessage-deflate");
            return new EchoSocket();
        });

        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/filterPath");
        EventSocket socket = new EventSocket();
        AtomicReference<HttpResponse> responseReference = new AtomicReference<>();
        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        upgradeRequest.addExtensions("permessage-deflate;client_no_context_takeover");
        JettyUpgradeListener upgradeListener = new JettyUpgradeListener()
        {
            @Override
            public void onHandshakeResponse(HttpRequest request, HttpResponse response)
            {
                responseReference.set(response);
            }
        };

        client.connect(socket, uri, upgradeRequest, upgradeListener).get(5, TimeUnit.SECONDS);
        HttpResponse httpResponse = responseReference.get();
        String extensions = httpResponse.getHeaders().get("Sec-WebSocket-Extensions");
        assertThat(extensions, is("permessage-deflate"));
    }
}