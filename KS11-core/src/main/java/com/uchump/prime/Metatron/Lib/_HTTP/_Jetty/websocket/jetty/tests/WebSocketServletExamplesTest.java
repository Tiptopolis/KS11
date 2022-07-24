package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.AuthenticationStore;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util.BasicAuthentication;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.ConstraintMapping;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.ConstraintSecurityHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.HashLoginService;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.SecurityHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.UserStore;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.security.authentication.BasicAuthenticator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.security.Constraint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.security.Credential;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples.MyAdvancedEchoServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples.MyAuthedServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples.MyEchoServlet;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebSocketServletExamplesTest
{
    private Server _server;
    private ServerConnector connector;
    private ServletContextHandler _context;

    @BeforeEach
    public void setup() throws Exception
    {
        _server = new Server();
        connector = new ServerConnector(_server);
        _server.addConnector(connector);

        _context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        _context.setContextPath("/");
        _context.setSecurityHandler(getSecurityHandler("user", "password", "testRealm"));
        _server.setHandler(_context);

        _context.addServlet(MyEchoServlet.class, "/echo");
        _context.addServlet(MyAdvancedEchoServlet.class, "/advancedEcho");
        _context.addServlet(MyAuthedServlet.class, "/authed");

        JettyWebSocketServletContainerInitializer.configure(_context, null);
        _server.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        _server.stop();
    }

    private static SecurityHandler getSecurityHandler(String username, String password, String realm)
    {

        HashLoginService loginService = new HashLoginService();
        UserStore userStore = new UserStore();
        userStore.addUser(username, Credential.getCredential(password), new String[]{"websocket"});
        loginService.setUserStore(userStore);
        loginService.setName(realm);

        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"**"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/authed/*");
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.addConstraintMapping(mapping);
        security.setAuthenticator(new BasicAuthenticator());
        security.setLoginService(loginService);

        return security;
    }

    @Test
    public void testEchoServlet() throws Exception
    {
        WebSocketClient client = new WebSocketClient();
        client.start();

        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/echo");
        EventSocket socket = new EventSocket();
        CompletableFuture<Session> connect = client.connect(socket, uri);
        try (Session session = connect.get(5, TimeUnit.SECONDS))
        {
            String message = "hello world";
            session.getRemote().sendString(message);

            String response = socket.textMessages.poll(5, TimeUnit.SECONDS);
            assertThat(response, is(message));
        }

        assertTrue(socket.closeLatch.await(10, TimeUnit.SECONDS));
    }

    @Test
    public void testAdvancedEchoServlet() throws Exception
    {
        WebSocketClient client = new WebSocketClient();
        client.start();

        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/advancedEcho");
        EventSocket socket = new EventSocket();

        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        upgradeRequest.setSubProtocols("text");
        CompletableFuture<Session> connect = client.connect(socket, uri, upgradeRequest);
        try (Session session = connect.get(5, TimeUnit.SECONDS))
        {
            String message = "hello world";
            session.getRemote().sendString(message);

            String response = socket.textMessages.poll(5, TimeUnit.SECONDS);
            assertThat(response, is(message));
        }

        assertTrue(socket.closeLatch.await(10, TimeUnit.SECONDS));
    }

    @Test
    public void testAuthedServlet() throws Exception
    {
        WebSocketClient client = new WebSocketClient();
        client.start();
        AuthenticationStore authenticationStore = client.getHttpClient().getAuthenticationStore();

        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/authed");

        BasicAuthentication basicAuthentication = new BasicAuthentication(uri, "testRealm", "user", "password");
        authenticationStore.addAuthentication(basicAuthentication);

        EventSocket socket = new EventSocket();
        CompletableFuture<Session> connect = client.connect(socket, uri);
        try (Session session = connect.get(5, TimeUnit.SECONDS))
        {
            String message = "hello world";
            session.getRemote().sendString(message);

            String response = socket.textMessages.poll(5, TimeUnit.SECONDS);
            assertThat(response, is(message));
        }

        assertTrue(socket.closeLatch.await(10, TimeUnit.SECONDS));
    }
}