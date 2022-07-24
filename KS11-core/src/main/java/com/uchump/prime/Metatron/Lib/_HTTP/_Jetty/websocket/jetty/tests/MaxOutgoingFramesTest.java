package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.net.URI;
import java.nio.channels.WritePendingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.RemoteEndpoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WriteCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.WebSocketCoreClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.AbstractExtension;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketServerComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketServletContainerInitializer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.util.FutureWriteCallback;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaxOutgoingFramesTest
{
    public static CountDownLatch outgoingBlocked;
    public static CountDownLatch firstFrameBlocked;

    private final EventSocket serverSocket = new EventSocket();
    private Server server;
    private ServerConnector connector;
    private WebSocketClient client;

    @BeforeEach
    public void start() throws Exception
    {
        outgoingBlocked = new CountDownLatch(1);
        firstFrameBlocked = new CountDownLatch(1);

        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        JettyWebSocketServletContainerInitializer.configure(contextHandler, (context, container) ->
        {
            container.addMapping("/", (req, resp) -> serverSocket);
            WebSocketComponents components = WebSocketServerComponents.getWebSocketComponents(context);
            components.getExtensionRegistry().register(BlockingOutgoingExtension.class.getName(), BlockingOutgoingExtension.class);
        });

        server.setHandler(contextHandler);

        client = new WebSocketClient();
        server.start();
        client.start();
    }

    @AfterEach
    public void stop() throws Exception
    {
        outgoingBlocked.countDown();
        server.stop();
        client.stop();
    }

    public static class BlockingOutgoingExtension extends AbstractExtension
    {
        @Override
        public String getName()
        {
            return BlockingOutgoingExtension.class.getName();
        }

        @Override
        public void sendFrame(Frame frame, Callback callback, boolean batch)
        {
            try
            {
                firstFrameBlocked.countDown();
                outgoingBlocked.await();
                super.sendFrame(frame, callback, batch);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static class CountingCallback implements WriteCallback
    {
        private final CountDownLatch successes;

        public CountingCallback(int count)
        {
            successes = new CountDownLatch(count);
        }

        @Override
        public void writeSuccess()
        {
            successes.countDown();
        }

        @Override
        public void writeFailed(Throwable t)
        {
            t.printStackTrace();
        }
    }

    @Test
    public void testMaxOutgoingFrames() throws Exception
    {
        // We need to have the frames queued but not yet sent, we do this by blocking in the ExtensionStack.
        WebSocketCoreClient coreClient = client.getBean(WebSocketCoreClient.class);
        coreClient.getExtensionRegistry().register(BlockingOutgoingExtension.class.getName(), BlockingOutgoingExtension.class);

        URI uri = URI.create("ws://localhost:" + connector.getLocalPort() + "/");
        EventSocket socket = new EventSocket();
        ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
        upgradeRequest.addExtensions(BlockingOutgoingExtension.class.getName());
        client.connect(socket, uri, upgradeRequest).get(5, TimeUnit.SECONDS);
        assertTrue(socket.openLatch.await(5, TimeUnit.SECONDS));

        int numFrames = 30;
        RemoteEndpoint remote = socket.session.getRemote();
        remote.setMaxOutgoingFrames(numFrames);

        // Verify that we can send up to numFrames without any problem.
        // First send will block in the Extension so it needs to be done in new thread, others frames will be queued.
        CountingCallback countingCallback = new CountingCallback(numFrames);
        new Thread(() -> remote.sendString("0", countingCallback)).start();
        assertTrue(firstFrameBlocked.await(5, TimeUnit.SECONDS));
        for (int i = 1; i < numFrames; i++)
        {
            remote.sendString(Integer.toString(i), countingCallback);
        }

        // Sending any more frames will result in WritePendingException.
        FutureWriteCallback callback = new FutureWriteCallback();
        remote.sendString("fail", callback);
        ExecutionException executionException = assertThrows(ExecutionException.class, () -> callback.get(5, TimeUnit.SECONDS));
        assertThat(executionException.getCause(), instanceOf(WritePendingException.class));

        // Check that all callbacks are succeeded when the server processes the frames.
        outgoingBlocked.countDown();
        assertTrue(countingCallback.successes.await(5, TimeUnit.SECONDS));

        // Close successfully.
        socket.session.close();
        assertTrue(serverSocket.closeLatch.await(5, TimeUnit.SECONDS));
        assertTrue(socket.closeLatch.await(5, TimeUnit.SECONDS));
    }
}