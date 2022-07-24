package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.WebSocketCoreClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketUpgradeHandler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebSocketUpgradeHandlerTest
{
    private Server server;
    private WebSocketCoreClient client;
    private URI serverUri;

    @BeforeEach
    public void before() throws Exception
    {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        WebSocketUpgradeHandler upgradeHandler = new WebSocketUpgradeHandler();
        upgradeHandler.addMapping("/path/echo", new TestWebSocketNegotiator(new EchoFrameHandler()));
        server.setHandler(upgradeHandler);
        server.start();

        client = new WebSocketCoreClient();
        client.start();

        serverUri = URI.create("ws://localhost:" + connector.getLocalPort());
    }

    @AfterEach
    public void after() throws Exception
    {
        server.stop();
        client.stop();
    }

    @Test
    public void testUpgradeByWebSocketUpgradeHandler() throws Exception
    {
        TestMessageHandler clientEndpoint = new TestMessageHandler();
        CoreSession coreSession = client.connect(clientEndpoint, serverUri.resolve("/path/echo")).get(5, TimeUnit.SECONDS);
        assertNotNull(coreSession);
        coreSession.close(Callback.NOOP);
        assertTrue(clientEndpoint.closeLatch.await(5, TimeUnit.SECONDS));
    }
}