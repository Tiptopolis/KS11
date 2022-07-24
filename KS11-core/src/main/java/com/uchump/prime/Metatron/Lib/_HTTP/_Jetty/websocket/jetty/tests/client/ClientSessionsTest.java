package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.client;

import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.DefaultHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.HandlerList;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.RemoteEndpoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketSessionListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.util.WSURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.CloseTrackingEndpoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EchoCreator;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.containsString;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientSessionsTest
{
    private Server server;

    @BeforeEach
    public void startServer() throws Exception
    {
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        ServletHolder holder = new ServletHolder(new JettyWebSocketServlet()
        {
            @Override
            public void configure(JettyWebSocketServletFactory factory)
            {
                factory.setIdleTimeout(Duration.ofSeconds(10));
                factory.setMaxTextMessageSize(1024 * 1024 * 2);
                factory.setCreator(new EchoCreator());
            }
        });
        context.addServlet(holder, "/ws");

        server.setHandler(new HandlerList(context, new DefaultHandler()));
        JettyWebSocketServletContainerInitializer.configure(context, null);

        server.start();
    }

    @AfterEach
    public void stopServer() throws Exception
    {
        server.stop();
    }

    @Test
    public void testBasicEchoFromClient() throws Exception
    {
        WebSocketClient client = new WebSocketClient();

        CountDownLatch onSessionCloseLatch = new CountDownLatch(1);

        client.addSessionListener(new WebSocketSessionListener()
        {
            @Override
            public void onWebSocketSessionClosed(Session session)
            {
                onSessionCloseLatch.countDown();
            }
        });

        client.start();
        try
        {
            CloseTrackingEndpoint cliSock = new CloseTrackingEndpoint();
            client.setIdleTimeout(Duration.ofSeconds(10));

            URI wsUri = WSURI.toWebsocket(server.getURI().resolve("/ws"));
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            request.setSubProtocols("echo");
            Future<Session> future = client.connect(cliSock, wsUri, request);

            try (Session sess = future.get(30000, TimeUnit.MILLISECONDS))
            {
                assertThat("Session", sess, notNullValue());
                assertThat("Session.open", sess.isOpen(), is(true));
                assertThat("Session.upgradeRequest", sess.getUpgradeRequest(), notNullValue());
                assertThat("Session.upgradeResponse", sess.getUpgradeResponse(), notNullValue());

                Collection<Session> sessions = client.getOpenSessions();
                assertThat("client.connectionManager.sessions.size", sessions.size(), is(1));

                RemoteEndpoint remote = sess.getRemote();
                remote.sendString("Hello World!");

                Collection<Session> open = client.getOpenSessions();
                assertThat("(Before Close) Open Sessions.size", open.size(), is(1));

                String received = cliSock.messageQueue.poll(5, TimeUnit.SECONDS);
                assertThat("Message", received, containsString("Hello World!"));

                sess.close(StatusCode.NORMAL, null);
            }

            cliSock.assertReceivedCloseEvent(30000, is(StatusCode.NORMAL));

            assertTrue(onSessionCloseLatch.await(5, TimeUnit.SECONDS), "Saw onSessionClose events");
            TimeUnit.SECONDS.sleep(1);

            Collection<Session> open = client.getOpenSessions();
            assertThat("(After Close) Open Sessions.size", open.size(), is(0));
        }
        finally
        {
            client.stop();
        }
    }
}