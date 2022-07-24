package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.client;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.Future;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.DefaultHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.HandlerList;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.util.WSURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.CloseTrackingEndpoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EchoSocket;

import static java.util.concurrent.TimeUnit.SECONDS;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.containsString;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

/**
 * This Regression Test Exists because of Client side Idle timeout, Read, and Parser bugs.
 */
public class SlowClientTest
{
    private Server server;
    private WebSocketClient client;

    @BeforeEach
    public void startClient() throws Exception
    {
        client = new WebSocketClient();
        client.setIdleTimeout(Duration.ofSeconds(60));
        client.start();
    }

    @BeforeEach
    public void startServer() throws Exception
    {
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        ServletHolder websocket = new ServletHolder(new JettyWebSocketServlet()
        {
            @Override
            public void configure(JettyWebSocketServletFactory factory)
            {
                factory.register(EchoSocket.class);
            }
        });
        context.addServlet(websocket, "/ws");

        server.setHandler(new HandlerList(context, new DefaultHandler()));
        JettyWebSocketServletContainerInitializer.configure(context, null);

        server.start();
    }

    @AfterEach
    public void stopClient() throws Exception
    {
        client.stop();
    }

    @AfterEach
    public void stopServer() throws Exception
    {
        server.stop();
    }

    @Test
    public void testClientSlowToSend() throws Exception
    {
        CloseTrackingEndpoint clientEndpoint = new CloseTrackingEndpoint();
        client.setIdleTimeout(Duration.ofSeconds(60));

        URI wsUri = WSURI.toWebsocket(server.getURI().resolve("/ws"));
        Future<Session> future = client.connect(clientEndpoint, wsUri);

        // Confirm connected
        Session session = future.get(5, SECONDS);

        int messageCount = 10;
        try
        {
            // Have client write slowly.
            ClientWriteThread writer = new ClientWriteThread(clientEndpoint.getSession());
            writer.setMessageCount(messageCount);
            writer.setMessage("Hello");
            writer.setSlowness(10);
            writer.start();
            writer.join();

            // Close
            clientEndpoint.getSession().close(StatusCode.NORMAL, "Done");

            // confirm close received on server
            clientEndpoint.assertReceivedCloseEvent(10000, is(StatusCode.NORMAL), containsString("Done"));
        }
        finally
        {
            if (session != null)
            {
                session.close();
            }
        }
    }
}