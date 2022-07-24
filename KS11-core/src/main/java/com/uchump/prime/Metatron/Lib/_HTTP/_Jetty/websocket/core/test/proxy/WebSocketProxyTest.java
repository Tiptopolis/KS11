package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.proxy;


import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging.StacklessLogging;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.AbstractHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.HandlerList;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.CoreClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.WebSocketCoreClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketUpgradeHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.EchoFrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.TestAsyncFrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.TestWebSocketNegotiator;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.containsString;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebSocketProxyTest
{
    private Server _server;
    private WebSocketCoreClient _client;
    private WebSocketProxy proxy;
    private EchoFrameHandler serverFrameHandler;
    private TestHandler testHandler;
    private Configuration.ConfigurationCustomizer defaultCustomizer;
    private URI proxyUri;

    private static class TestHandler extends AbstractHandler
    {
        public void blockServerUpgradeRequests()
        {
            blockServerUpgradeRequests = true;
        }

        public boolean blockServerUpgradeRequests = false;

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException
        {
            if (baseRequest.getHeader("Upgrade") != null)
            {
                if (blockServerUpgradeRequests && target.startsWith("/server/"))
                {
                    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR_500);
                    baseRequest.setHandled(true);
                }
            }
        }
    }

    @BeforeEach
    public void start() throws Exception
    {
        _server = new Server();
        ServerConnector connector = new ServerConnector(_server);
        _server.addConnector(connector);

        HandlerList handlers = new HandlerList();
        testHandler = new TestHandler();
        handlers.addHandler(testHandler);

        defaultCustomizer = new Configuration.ConfigurationCustomizer();
        defaultCustomizer.setIdleTimeout(Duration.ofSeconds(3));

        ContextHandler serverContext = new ContextHandler("/server");
        serverFrameHandler = new EchoFrameHandler("SERVER");
        WebSocketNegotiator negotiator = new TestWebSocketNegotiator(serverFrameHandler, defaultCustomizer);
        WebSocketUpgradeHandler upgradeHandler = new WebSocketUpgradeHandler();
        upgradeHandler.addMapping("/*", negotiator);
        serverContext.setHandler(upgradeHandler);
        handlers.addHandler(serverContext);

        ContextHandler proxyContext = new ContextHandler("/proxy");
        negotiator = WebSocketNegotiator.from(negotiation -> proxy.client2Proxy, defaultCustomizer);
        upgradeHandler = new WebSocketUpgradeHandler();
        upgradeHandler.addMapping("/*", negotiator);
        proxyContext.setHandler(upgradeHandler);
        handlers.addHandler(proxyContext);

        _server.setHandler(handlers);
        _server.start();
        _client = new WebSocketCoreClient();
        _client.start();

        URI uri = new URI("ws://localhost:" + connector.getLocalPort());
        proxyUri = uri.resolve("/proxy");
        proxy = new WebSocketProxy(_client, uri.resolve("/server"));
    }

    @AfterEach
    public void stop() throws Exception
    {
        _client.stop();
        _server.stop();
    }

    public void awaitProxyClose(WebSocketProxy.Client2Proxy client2Proxy, WebSocketProxy.Server2Proxy server2Proxy) throws Exception
    {
        if (client2Proxy != null && !client2Proxy.closed.await(5, TimeUnit.SECONDS))
            throw new TimeoutException("client2Proxy close timeout");

        if (server2Proxy != null && !server2Proxy.closed.await(5, TimeUnit.SECONDS))
            throw new TimeoutException("server2Proxy close timeout");
    }

    @Test
    public void testEcho() throws Exception
    {
        TestAsyncFrameHandler clientFrameHandler = new TestAsyncFrameHandler("CLIENT");
        WebSocketProxy.Client2Proxy proxyClientSide = proxy.client2Proxy;
        WebSocketProxy.Server2Proxy proxyServerSide = proxy.server2Proxy;

        CoreClientUpgradeRequest upgradeRequest = CoreClientUpgradeRequest.from(_client, proxyUri, clientFrameHandler);
        upgradeRequest.setConfiguration(defaultCustomizer);
        CompletableFuture<CoreSession> response = _client.connect(upgradeRequest);

        response.get(5, TimeUnit.SECONDS);
        clientFrameHandler.sendText("hello world");
        clientFrameHandler.close(CloseStatus.NORMAL, "standard close");
        assertTrue(clientFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
        assertTrue(serverFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
        awaitProxyClose(proxyClientSide, proxyServerSide);

        assertThat(proxyClientSide.getState(), is(WebSocketProxy.State.CLOSED));
        assertThat(proxyServerSide.getState(), is(WebSocketProxy.State.CLOSED));

        assertThat(Objects.requireNonNull(proxyClientSide.receivedFrames.poll()).getPayloadAsUTF8(), is("hello world"));
        assertThat(Objects.requireNonNull(serverFrameHandler.receivedFrames.poll()).getPayloadAsUTF8(), is("hello world"));
        assertThat(Objects.requireNonNull(proxyServerSide.receivedFrames.poll()).getPayloadAsUTF8(), is("hello world"));
        assertThat(Objects.requireNonNull(clientFrameHandler.receivedFrames.poll()).getPayloadAsUTF8(), is("hello world"));

        assertThat(CloseStatus.getCloseStatus(proxyClientSide.receivedFrames.poll()).getReason(), is("standard close"));
        assertThat(CloseStatus.getCloseStatus(serverFrameHandler.receivedFrames.poll()).getReason(), is("standard close"));
        assertThat(CloseStatus.getCloseStatus(proxyServerSide.receivedFrames.poll()).getReason(), is("standard close"));
        assertThat(CloseStatus.getCloseStatus(clientFrameHandler.receivedFrames.poll()).getReason(), is("standard close"));

        assertNull(proxyClientSide.receivedFrames.poll());
        assertNull(serverFrameHandler.receivedFrames.poll());
        assertNull(proxyServerSide.receivedFrames.poll());
        assertNull(clientFrameHandler.receivedFrames.poll());
    }

    @Test
    public void testFailServerUpgrade() throws Exception
    {
        testHandler.blockServerUpgradeRequests();
        WebSocketProxy.Client2Proxy proxyClientSide = proxy.client2Proxy;
        WebSocketProxy.Server2Proxy proxyServerSide = proxy.server2Proxy;

        TestAsyncFrameHandler clientFrameHandler = new TestAsyncFrameHandler("CLIENT");
        try (StacklessLogging ignored = new StacklessLogging(WebSocketCoreSession.class))
        {
            CoreClientUpgradeRequest upgradeRequest = CoreClientUpgradeRequest.from(_client, proxyUri, clientFrameHandler);
            upgradeRequest.setConfiguration(defaultCustomizer);
            CompletableFuture<CoreSession> response = _client.connect(upgradeRequest);
            response.get(5, TimeUnit.SECONDS);
            clientFrameHandler.sendText("hello world");
            clientFrameHandler.close();
            assertTrue(clientFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
            awaitProxyClose(proxyClientSide, null);
        }

        assertNull(proxyClientSide.receivedFrames.poll());
        assertThat(proxyClientSide.getState(), is(WebSocketProxy.State.FAILED));

        assertNull(proxyServerSide.receivedFrames.poll());
        assertThat(proxyServerSide.getState(), is(WebSocketProxy.State.FAILED));

        assertFalse(serverFrameHandler.openLatch.await(250, TimeUnit.MILLISECONDS));

        CloseStatus closeStatus = CloseStatus.getCloseStatus(clientFrameHandler.receivedFrames.poll());
        assertThat(closeStatus.getCode(), is(CloseStatus.SERVER_ERROR));
        assertThat(closeStatus.getReason(), containsString("Failed to upgrade to websocket: Unexpected HTTP Response"));
    }

    @Test
    public void testClientError() throws Exception
    {
        TestAsyncFrameHandler clientFrameHandler = new TestAsyncFrameHandler("CLIENT")
        {
            @Override
            public void onOpen(CoreSession coreSession, Callback callback)
            {
                throw new IllegalStateException("simulated client onOpen error");
            }
        };
        WebSocketProxy.Client2Proxy proxyClientSide = proxy.client2Proxy;
        WebSocketProxy.Server2Proxy proxyServerSide = proxy.server2Proxy;

        try (StacklessLogging ignored = new StacklessLogging(WebSocketCoreSession.class))
        {
            CoreClientUpgradeRequest upgradeRequest = CoreClientUpgradeRequest.from(_client, proxyUri, clientFrameHandler);
            upgradeRequest.setConfiguration(defaultCustomizer);
            CompletableFuture<CoreSession> response = _client.connect(upgradeRequest);
            Exception e = assertThrows(ExecutionException.class, () -> response.get(5, TimeUnit.SECONDS));
            assertThat(e.getMessage(), containsString("simulated client onOpen error"));
            assertTrue(clientFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
            assertTrue(serverFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
            awaitProxyClose(proxyClientSide, proxyServerSide);
        }

        CloseStatus closeStatus = CloseStatus.getCloseStatus(proxyClientSide.receivedFrames.poll());
        assertThat(closeStatus.getCode(), is(CloseStatus.SERVER_ERROR));
        assertThat(closeStatus.getReason(), containsString("simulated client onOpen error"));
        assertThat(proxyClientSide.getState(), is(WebSocketProxy.State.FAILED));

        closeStatus = CloseStatus.getCloseStatus(serverFrameHandler.receivedFrames.poll());
        assertThat(closeStatus.getCode(), is(CloseStatus.SERVER_ERROR));
        assertThat(closeStatus.getReason(), containsString("simulated client onOpen error"));

        assertNull(proxyServerSide.receivedFrames.poll());
        assertNull(clientFrameHandler.receivedFrames.poll());
    }

    @Test
    public void testServerError() throws Exception
    {
        serverFrameHandler.throwOnFrame();
        WebSocketProxy.Client2Proxy proxyClientSide = proxy.client2Proxy;
        WebSocketProxy.Server2Proxy proxyServerSide = proxy.server2Proxy;

        TestAsyncFrameHandler clientFrameHandler = new TestAsyncFrameHandler("CLIENT");
        CoreClientUpgradeRequest upgradeRequest = CoreClientUpgradeRequest.from(_client, proxyUri, clientFrameHandler);
        upgradeRequest.setConfiguration(defaultCustomizer);
        CompletableFuture<CoreSession> response = _client.connect(upgradeRequest);

        response.get(5, TimeUnit.SECONDS);
        clientFrameHandler.sendText("hello world");
        assertTrue(clientFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
        assertTrue(serverFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
        awaitProxyClose(proxyClientSide, proxyServerSide);

        CloseStatus closeStatus;
        Frame frame;

        // Client2Proxy
        frame = proxyClientSide.receivedFrames.poll();
        assertNotNull(frame);
        assertThat(frame.getOpCode(), is(OpCode.TEXT));
        assertThat(frame.getPayloadAsUTF8(), is("hello world"));

        // Server
        frame = serverFrameHandler.receivedFrames.poll();
        assertNotNull(frame);
        assertThat(frame.getOpCode(), is(OpCode.TEXT));
        assertThat(frame.getPayloadAsUTF8(), is("hello world"));
        frame = serverFrameHandler.receivedFrames.poll();
        assertNull(frame);

        // Server2Proxy
        frame = proxyServerSide.receivedFrames.poll();
        assertNotNull(frame);
        closeStatus = CloseStatus.getCloseStatus(frame);
        assertThat(closeStatus.getCode(), is(CloseStatus.SERVER_ERROR));
        assertThat(closeStatus.getReason(), is("intentionally throwing in server onFrame()"));

        // Client
        frame = clientFrameHandler.receivedFrames.poll();
        assertNotNull(frame);
        closeStatus = CloseStatus.getCloseStatus(frame);
        assertThat(closeStatus.getCode(), is(CloseStatus.SERVER_ERROR));
        assertThat(closeStatus.getReason(), is("intentionally throwing in server onFrame()"));

        // Client2Proxy receives no close response because is error close
        assertNull(proxyClientSide.receivedFrames.poll());

        // Check Proxy is in expected final state
        assertNull(proxyClientSide.receivedFrames.poll());
        assertNull(proxyServerSide.receivedFrames.poll());
        assertThat(proxyClientSide.getState(), is(WebSocketProxy.State.FAILED));
        assertThat(proxyServerSide.getState(), is(WebSocketProxy.State.FAILED));
    }

    @Test
    public void testServerErrorClientNoResponse() throws Exception
    {
        serverFrameHandler.throwOnFrame();
        WebSocketProxy.Client2Proxy proxyClientSide = proxy.client2Proxy;
        WebSocketProxy.Server2Proxy proxyServerSide = proxy.server2Proxy;

        TestAsyncFrameHandler clientFrameHandler = new TestAsyncFrameHandler("CLIENT")
        {
            @Override
            public void onFrame(Frame frame, Callback callback)
            {
                LOG.info("[{}] onFrame {}", name, frame);
                receivedFrames.offer(Frame.copy(frame));
            }
        };

        CoreClientUpgradeRequest upgradeRequest = CoreClientUpgradeRequest.from(_client, proxyUri, clientFrameHandler);
        upgradeRequest.setConfiguration(defaultCustomizer);
        CompletableFuture<CoreSession> response = _client.connect(upgradeRequest);
        response.get(5, TimeUnit.SECONDS);
        clientFrameHandler.sendText("hello world");
        assertTrue(clientFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
        assertTrue(serverFrameHandler.closeLatch.await(5, TimeUnit.SECONDS));
        awaitProxyClose(proxyClientSide, proxyServerSide);

        CloseStatus closeStatus;
        Frame frame;

        // Client2Proxy
        frame = proxyClientSide.receivedFrames.poll();
        assertNotNull(frame);
        assertThat(frame.getOpCode(), is(OpCode.TEXT));
        assertThat(frame.getPayloadAsUTF8(), is("hello world"));

        // Server
        frame = serverFrameHandler.receivedFrames.poll();
        assertNotNull(frame);
        assertThat(frame.getOpCode(), is(OpCode.TEXT));
        assertThat(frame.getPayloadAsUTF8(), is("hello world"));
        assertNull(serverFrameHandler.receivedFrames.poll());

        // Server2Proxy
        frame = proxyServerSide.receivedFrames.poll();
        closeStatus = CloseStatus.getCloseStatus(frame);
        assertThat(closeStatus.getCode(), is(CloseStatus.SERVER_ERROR));
        assertThat(closeStatus.getReason(), is("intentionally throwing in server onFrame()"));

        // Client
        frame = clientFrameHandler.receivedFrames.poll();
        closeStatus = CloseStatus.getCloseStatus(frame);
        assertThat(closeStatus.getCode(), is(CloseStatus.SERVER_ERROR));
        assertThat(closeStatus.getReason(), is("intentionally throwing in server onFrame()"));
        assertNull(clientFrameHandler.receivedFrames.poll());

        // Client2Proxy does NOT receive close response from the client and fails
        assertNull(proxyClientSide.receivedFrames.poll());
        assertThat(proxyClientSide.getState(), is(WebSocketProxy.State.FAILED));

        // Server2Proxy is failed by the Client2Proxy
        assertNull(proxyServerSide.receivedFrames.poll());
        assertThat(proxyServerSide.getState(), is(WebSocketProxy.State.FAILED));
    }
}