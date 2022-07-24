package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContainerInitializerHolder;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.DeflaterPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.InflaterPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketServerComponents;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.zip.Deflater;

public class WebSocketServerComponentsTest
{
    private Server server;
    private ServletContextHandler contextHandler;
    private WebSocketComponents components;

    @BeforeEach
    public void before()
    {
        server = new Server();
        contextHandler = new ServletContextHandler();
        server.setHandler(contextHandler);
    }

    @AfterEach
    public void after() throws Exception
    {
        server.stop();
    }

    @Test
    public void testComponentsInsideServletContainerInitializer() throws Exception
    {
        // ensureWebSocketComponents can only be called when the server is starting.
        contextHandler.addServletContainerInitializer(new ServletContainerInitializerHolder((c, ctx) ->
            components = WebSocketServerComponents.ensureWebSocketComponents(server, contextHandler.getServletContext())));

        // Components is created only when the server is started.
        assertNull(components);
        server.start();
        assertNotNull(components);

        // Components is started when it is created.
        assertTrue(components.isStarted());
        DeflaterPool deflaterPool = components.getDeflaterPool();
        InflaterPool inflaterPool = components.getInflaterPool();

        // The components is stopped with the ServletContext.
        contextHandler.stop();
        assertTrue(components.isStopped());

        // By default the default CompressionPools from the server are used, these should not be stopped with the context.
        assertTrue(inflaterPool.isStarted());
        assertTrue(deflaterPool.isStarted());
    }

    @Test
    public void testCompressionPoolsManagedByContext() throws Exception
    {
        ContextHandler.Context servletContext = contextHandler.getServletContext();

        // Use a custom InflaterPool and DeflaterPool that are not started or managed.
        InflaterPool inflaterPool = new InflaterPool(333, false);
        DeflaterPool deflaterPool = new DeflaterPool(333, Deflater.BEST_SPEED, false);
        servletContext.setAttribute(WebSocketServerComponents.WEBSOCKET_DEFLATER_POOL_ATTRIBUTE, deflaterPool);
        servletContext.setAttribute(WebSocketServerComponents.WEBSOCKET_INFLATER_POOL_ATTRIBUTE, inflaterPool);

        // ensureWebSocketComponents can only be called when the server is starting.
        contextHandler.addServletContainerInitializer(new ServletContainerInitializerHolder((c, ctx) ->
            components = WebSocketServerComponents.ensureWebSocketComponents(server, servletContext)));

        // Components is created only when the server is started.
        assertNull(components);
        server.start();
        assertNotNull(components);

        // Components is started when it is created.
        assertTrue(components.isStarted());

        // The components uses the CompressionPools set as servletContext attributes.
        assertThat(components.getInflaterPool(), is(inflaterPool));
        assertThat(components.getDeflaterPool(), is(deflaterPool));
        assertTrue(inflaterPool.isStarted());
        assertTrue(deflaterPool.isStarted());

        // The components is stopped with the ServletContext.
        contextHandler.stop();
        assertTrue(components.isStopped());

        // The inflater and deflater pools are stopped as they are not managed by the server.
        assertTrue(inflaterPool.isStopped());
        assertTrue(deflaterPool.isStopped());
    }

    @Test
    public void testCompressionPoolsManagedByServer() throws Exception
    {
        ContextHandler.Context servletContext = contextHandler.getServletContext();

        // Use a custom InflaterPool and DeflaterPool that are not started or managed.
        InflaterPool inflaterPool = new InflaterPool(333, false);
        DeflaterPool deflaterPool = new DeflaterPool(333, Deflater.BEST_SPEED, false);
        server.addBean(inflaterPool);
        server.addBean(deflaterPool);

        // ensureWebSocketComponents can only be called when the server is starting.
        contextHandler.addServletContainerInitializer(new ServletContainerInitializerHolder((c, ctx) ->
            components = WebSocketServerComponents.ensureWebSocketComponents(server, servletContext)));

        // The CompressionPools will only be started with the server.
        assertTrue(inflaterPool.isStopped());
        assertTrue(deflaterPool.isStopped());
        server.start();
        assertThat(components.getInflaterPool(), is(inflaterPool));
        assertThat(components.getDeflaterPool(), is(deflaterPool));
        assertTrue(inflaterPool.isStarted());
        assertTrue(deflaterPool.isStarted());

        // The components is stopped with the ServletContext, but the CompressionPools are stopped with the server.
        contextHandler.stop();
        assertTrue(components.isStopped());
        assertTrue(inflaterPool.isStarted());
        assertTrue(deflaterPool.isStarted());
        server.stop();
        assertTrue(inflaterPool.isStopped());
        assertTrue(deflaterPool.isStopped());
    }
}