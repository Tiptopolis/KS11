package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;


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
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.util.WSURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.CloseTrackingEndpoint;

import static java.util.concurrent.TimeUnit.SECONDS;
import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;

/**
 * This Regression Test Exists because of Server side Idle timeout, Write, and Generator bugs.
 */
public class SlowServerTest
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
                factory.register(SlowServerEndpoint.class);
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
    public void testServerSlowToSend() throws Exception
    {
        CloseTrackingEndpoint clientEndpoint = new CloseTrackingEndpoint();
        client.setIdleTimeout(Duration.ofSeconds(60));

        URI wsUri = WSURI.toWebsocket(server.getURI().resolve("/ws"));
        Future<Session> future = client.connect(clientEndpoint, wsUri);

        Session session = null;
        try
        {
            // Confirm connected
            session = future.get(5, SECONDS);

            int messageCount = 10;

            session.getRemote().sendString("send-slow|" + messageCount);

            // Verify receive
            LinkedBlockingQueue<String> responses = clientEndpoint.messageQueue;

            for (int i = 0; i < messageCount; i++)
            {
                String response = responses.poll(5, SECONDS);
                assertThat("Server Message[" + i + "]", response, is("Hello/" + i + "/"));
            }
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