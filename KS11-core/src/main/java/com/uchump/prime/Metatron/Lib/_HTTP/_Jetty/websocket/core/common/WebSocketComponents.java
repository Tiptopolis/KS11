package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common;

import java.util.concurrent.Executor;
import java.util.zip.Deflater;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.MappedByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.DecoratedObjectFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.CompressionPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.DeflaterPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.InflaterPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.QueuedThreadPool;

/**
 * A collection of components which are the resources needed for websockets such as
 * {@link ByteBufferPool}, {@link WebSocketExtensionRegistry}, and {@link DecoratedObjectFactory}.
 */
public class WebSocketComponents extends ContainerLifeCycle
{
    private final DecoratedObjectFactory _objectFactory;
    private final WebSocketExtensionRegistry _extensionRegistry;
    private final Executor _executor;
    private final ByteBufferPool _bufferPool;
    private final InflaterPool _inflaterPool;
    private final DeflaterPool _deflaterPool;

    public WebSocketComponents()
    {
        this(null, null, null, null, null);
    }

    public WebSocketComponents(WebSocketExtensionRegistry extensionRegistry, DecoratedObjectFactory objectFactory,
                               ByteBufferPool bufferPool, InflaterPool inflaterPool, DeflaterPool deflaterPool)
    {
        this (extensionRegistry, objectFactory, bufferPool, inflaterPool, deflaterPool, null);
    }

    public WebSocketComponents(WebSocketExtensionRegistry extensionRegistry, DecoratedObjectFactory objectFactory,
                               ByteBufferPool bufferPool, InflaterPool inflaterPool, DeflaterPool deflaterPool, Executor executor)
    {
        _extensionRegistry = (extensionRegistry == null) ? new WebSocketExtensionRegistry() : extensionRegistry;
        _objectFactory = (objectFactory == null) ? new DecoratedObjectFactory() : objectFactory;
        _bufferPool = (bufferPool == null) ? new MappedByteBufferPool() : bufferPool;
        _inflaterPool = (inflaterPool == null) ? new InflaterPool(CompressionPool.DEFAULT_CAPACITY, true) : inflaterPool;
        _deflaterPool = (deflaterPool == null) ? new DeflaterPool(CompressionPool.DEFAULT_CAPACITY, Deflater.DEFAULT_COMPRESSION, true) : deflaterPool;

        if (executor == null)
        {
            QueuedThreadPool threadPool = new QueuedThreadPool();
            threadPool.setName("WebSocket@" + hashCode());
            _executor = threadPool;
        }
        else
        {
            _executor = executor;
        }

        addBean(_inflaterPool);
        addBean(_deflaterPool);
        addBean(_bufferPool);
        addBean(_extensionRegistry);
        addBean(_objectFactory);
        addBean(_executor);
    }

    public ByteBufferPool getBufferPool()
    {
        return _bufferPool;
    }

    public Executor getExecutor()
    {
        return _executor;
    }

    public WebSocketExtensionRegistry getExtensionRegistry()
    {
        return _extensionRegistry;
    }

    public DecoratedObjectFactory getObjectFactory()
    {
        return _objectFactory;
    }

    public InflaterPool getInflaterPool()
    {
        return _inflaterPool;
    }

    public DeflaterPool getDeflaterPool()
    {
        return _deflaterPool;
    }
}