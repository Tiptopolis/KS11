package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.ContentResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.BadMessageException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPolicy;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketConnect;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketMessage;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.CoreClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.config.JettyWebSocketClientConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.WebSocketSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.xml.XmlConfiguration;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.containsString;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JettyClientClassLoaderTest
{
    private final WebAppTester webAppTester = new WebAppTester();
    private final HttpClient httpClient = new HttpClient();

    @FunctionalInterface
    interface ThrowingRunnable
    {
        void run() throws Exception;
    }

    public void start(ThrowingRunnable configuration) throws Exception
    {
        configuration.run();
        webAppTester.start();
        httpClient.start();
    }

    @AfterEach
    public void after() throws Exception
    {
        httpClient.stop();
        webAppTester.stop();
    }

    @WebSocket
    public static class ClientSocket
    {
        LinkedBlockingQueue<String> textMessages = new LinkedBlockingQueue<>();

        @OnWebSocketConnect
        public void onOpen(Session session) throws Exception
        {
            session.getRemote().sendString("ContextClassLoader: " + Thread.currentThread().getContextClassLoader());
        }

        @OnWebSocketMessage
        public void onMessage(String message)
        {
            textMessages.add(message);
        }
    }

    @WebServlet("/servlet")
    public static class WebSocketClientServlet extends HttpServlet
    {
        private final WebSocketClient clientContainer = new WebSocketClient();

        @Override
        public void init(ServletConfig config) throws ServletException
        {
            try
            {
                clientContainer.start();
            }
            catch (Exception e)
            {
                throw new ServletException(e);
            }
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        {
            URI wsEchoUri = URI.create("ws://localhost:" + req.getServerPort() + "/echo");
            ClientSocket clientSocket = new ClientSocket();

            try (Session ignored = clientContainer.connect(clientSocket, wsEchoUri).get(5, TimeUnit.SECONDS))
            {
                String recv = clientSocket.textMessages.poll(5, TimeUnit.SECONDS);
                assertNotNull(recv);
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(recv);
                resp.getWriter().println("ClientClassLoader: " + clientContainer.getClass().getClassLoader());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @WebServlet("/")
    public static class EchoServlet extends JettyWebSocketServlet
    {
        @Override
        protected void configure(JettyWebSocketServletFactory factory)
        {
            factory.register(EchoSocket.class);
        }
    }

    @WebSocket
    public static class EchoSocket
    {
        @OnWebSocketMessage
        public void onMessage(Session session, String message) throws Exception
        {
            session.getRemote().sendString(message);
        }
    }

    public WebAppTester.WebApp createWebSocketWebapp(String contextName) throws Exception
    {
        WebAppTester.WebApp app = webAppTester.createWebApp(contextName);

        // Copy over the individual jars required for Javax WebSocket.
        app.createWebInf();
        app.copyLib(WebSocketPolicy.class, "websocket-jetty-api.jar");
        app.copyLib(WebSocketClient.class, "websocket-jetty-client.jar");
        app.copyLib(WebSocketSession.class, "websocket-jetty-common.jar");
        app.copyLib(ContainerLifeCycle.class, "jetty-util.jar");
        app.copyLib(CoreClientUpgradeRequest.class, "websocket-core-client.jar");
        app.copyLib(WebSocketComponents.class, "websocket-core-common.jar");
        app.copyLib(Response.class, "jetty-client.jar");
        app.copyLib(ByteBufferPool.class, "jetty-io.jar");
        app.copyLib(BadMessageException.class, "jetty-http.jar");
        app.copyLib(XmlConfiguration.class, "jetty-xml.jar");

        return app;
    }

    @Test
    public void websocketProvidedByServer() throws Exception
    {
        start(() ->
        {
            WebAppTester.WebApp app1 = webAppTester.createWebApp("/app");
            app1.addConfiguration(new JettyWebSocketClientConfiguration());
            app1.createWebInf();
            app1.copyClass(WebSocketClientServlet.class);
            app1.copyClass(ClientSocket.class);
            app1.deploy();

            WebAppTester.WebApp app2 = webAppTester.createWebApp("/echo");
            app2.addConfiguration(new JettyWebSocketConfiguration());
            app2.createWebInf();
            app2.copyClass(EchoServlet.class);
            app2.copyClass(EchoSocket.class);
            app2.deploy();
        });

        // After hitting each WebApp we will get 200 response if test succeeds.
        ContentResponse response = httpClient.GET(webAppTester.getServerUri().resolve("/app/servlet"));
        assertThat(response.getStatus(), is(HttpStatus.OK_200));

        // The ContextClassLoader in the WebSocketClients onOpen was the WebAppClassloader.
        assertThat(response.getContentAsString(), containsString("ContextClassLoader: WebAppClassLoader"));

        // Verify that we used Servers version of WebSocketClient.
        ClassLoader serverClassLoader = webAppTester.getServer().getClass().getClassLoader();
        assertThat(response.getContentAsString(), containsString("ClientClassLoader: " + serverClassLoader));
    }

    /**
     * This reproduces some classloading issue with MethodHandles in JDK14-110, This has been fixed in JDK16.
     * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8244090">JDK-8244090</a>
     */
    @DisabledOnJre({JRE.JAVA_14, JRE.JAVA_15})
    @Test
    public void websocketProvidedByWebApp() throws Exception
    {
        start(() ->
        {
            WebAppTester.WebApp app1 = createWebSocketWebapp("/app");
            app1.createWebInf();
            app1.copyClass(WebSocketClientServlet.class);
            app1.copyClass(ClientSocket.class);
            app1.deploy();

            WebAppTester.WebApp app2 = webAppTester.createWebApp("/echo");
            app2.addConfiguration(new JettyWebSocketConfiguration());
            app2.createWebInf();
            app2.copyClass(EchoServlet.class);
            app2.copyClass(EchoSocket.class);
            app2.deploy();
        });

        // After hitting each WebApp we will get 200 response if test succeeds.
        ContentResponse response = httpClient.GET(webAppTester.getServerUri().resolve("/app/servlet"));
        assertThat(response.getStatus(), is(HttpStatus.OK_200));

        // The ContextClassLoader in the WebSocketClients onOpen was the WebAppClassloader.
        assertThat(response.getContentAsString(), containsString("ContextClassLoader: WebAppClassLoader"));

        // Verify that we used WebApps version of WebSocketClient.
        assertThat(response.getContentAsString(), containsString("ClientClassLoader: WebAppClassLoader"));
    }
}