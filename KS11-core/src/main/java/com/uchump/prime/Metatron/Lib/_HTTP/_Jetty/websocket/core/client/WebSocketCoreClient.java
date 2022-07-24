package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.DecoratedObjectFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.internal.HttpClientProvider;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketExtensionRegistry;

public class WebSocketCoreClient extends ContainerLifeCycle
{
    public static final String WEBSOCKET_CORECLIENT_ATTRIBUTE = WebSocketCoreClient.class.getName();

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketCoreClient.class);
    private final HttpClient httpClient;
    private final WebSocketComponents components;
    private ClassLoader classLoader;

    public WebSocketCoreClient()
    {
        this(null, new WebSocketComponents());
    }

    public WebSocketCoreClient(WebSocketComponents webSocketComponents)
    {
        this(null, webSocketComponents);
    }

    public WebSocketCoreClient(HttpClient httpClient, WebSocketComponents webSocketComponents)
    {
        if (httpClient == null)
            httpClient = Objects.requireNonNull(HttpClientProvider.get());
        if (httpClient.getExecutor() == null)
            httpClient.setExecutor(webSocketComponents.getExecutor());

        this.classLoader = Thread.currentThread().getContextClassLoader();
        this.httpClient = httpClient;
        this.components = webSocketComponents;
        addBean(httpClient);
        addBean(webSocketComponents);
    }

    public ClassLoader getClassLoader()
    {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader)
    {
        this.classLoader = Objects.requireNonNull(classLoader);
    }

    public CompletableFuture<CoreSession> connect(FrameHandler frameHandler, URI wsUri) throws IOException
    {
        CoreClientUpgradeRequest request = CoreClientUpgradeRequest.from(this, wsUri, frameHandler);
        return connect(request);
    }

    public CompletableFuture<CoreSession> connect(CoreClientUpgradeRequest request) throws IOException
    {
        if (!isStarted())
            throw new IllegalStateException(WebSocketCoreClient.class.getSimpleName() + "@" + this.hashCode() + " is not started");

        // Validate Requested Extensions
        for (ExtensionConfig reqExt : request.getExtensions())
        {
            if (!components.getExtensionRegistry().isAvailable(reqExt.getName()))
            {
                throw new IllegalArgumentException("Requested extension [" + reqExt.getName() + "] is not installed");
            }
        }

        for (Request.Listener l : getBeans(Request.Listener.class))
        {
            request.listener(l);
        }

        if (LOG.isDebugEnabled())
            LOG.debug("connect to websocket {}", request.getURI());

        return request.sendAsync();
    }

    public WebSocketExtensionRegistry getExtensionRegistry()
    {
        return components.getExtensionRegistry();
    }

    public HttpClient getHttpClient()
    {
        return httpClient;
    }

    public DecoratedObjectFactory getObjectFactory()
    {
        return components.getObjectFactory();
    }

    public WebSocketComponents getWebSocketComponents()
    {
        return components;
    }
}