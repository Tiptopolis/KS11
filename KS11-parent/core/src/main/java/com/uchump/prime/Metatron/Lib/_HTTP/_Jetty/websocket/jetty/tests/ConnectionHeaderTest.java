package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;


import java.net.URI;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpFields;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.JettyUpgradeListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

public class ConnectionHeaderTest
{
    private static WebSocketClient client;
    private static Server server;
    private static ServerConnector connector;

    @BeforeAll
    public static void startContainers() throws Exception
    {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        JettyWebSocketServletContainerInitializer.configure(contextHandler, (servletContext, container) ->
            container.addMapping("/echo", EchoSocket.class));

        server.setHandler(contextHandler);
        server.start();

        client = new WebSocketClient();
        client.start();
    }

    @AfterAll
    public static void stopContainers() throws Exception
    {
        client.stop();
        server.stop();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Upgrade", "keep-alive, Upgrade", "close, Upgrade"})
    public void testConnectionKeepAlive(String connectionHeaderValue) throws Exception
    {
        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/echo");
        JettyUpgradeListener upgradeListener = new JettyUpgradeListener()
        {
            @Override
            public void onHandshakeRequest(HttpRequest request)
            {
                HttpFields fields = request.getHeaders();
                if (!(fields instanceof HttpFields.Mutable))
                    throw new IllegalStateException(fields.getClass().getName());

                // Replace the default connection header value with a custom one.
                HttpFields.Mutable headers = (HttpFields.Mutable)fields;
                headers.put(HttpHeader.CONNECTION, connectionHeaderValue);
            }
        };

        EventSocket clientEndpoint = new EventSocket();
        try (Session session = client.connect(clientEndpoint, uri, null, upgradeListener).get(5, TimeUnit.SECONDS))
        {
            // Generate text frame
            String msg = "this is an echo ... cho ... ho ... o";
            session.getRemote().sendString(msg);

            // Read frame (hopefully text frame)
            String response = clientEndpoint.textMessages.poll(5, TimeUnit.SECONDS);
            assertThat("Text Frame.status code", response, is(msg));
        }
    }
}