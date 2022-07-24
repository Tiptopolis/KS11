package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common;

import java.io.IOException;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.Objects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Dumpable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.SuspendToken;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.UpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.UpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketBehavior;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WriteCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;

public class WebSocketSession implements Session, SuspendToken, Dumpable
{
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketSession.class);
    private final CoreSession coreSession;
    private final JettyWebSocketFrameHandler frameHandler;
    private final JettyWebSocketRemoteEndpoint remoteEndpoint;
    private final UpgradeRequest upgradeRequest;
    private final UpgradeResponse upgradeResponse;

    public WebSocketSession(WebSocketContainer container, CoreSession coreSession, JettyWebSocketFrameHandler frameHandler)
    {
        this.frameHandler = Objects.requireNonNull(frameHandler);
        this.coreSession = Objects.requireNonNull(coreSession);
        this.upgradeRequest = frameHandler.getUpgradeRequest();
        this.upgradeResponse = frameHandler.getUpgradeResponse();
        this.remoteEndpoint = new JettyWebSocketRemoteEndpoint(coreSession, frameHandler.getBatchMode());
        container.notifySessionListeners((listener) -> listener.onWebSocketSessionCreated(this));
    }

    @Override
    public void close()
    {
        coreSession.close(StatusCode.NORMAL, null, Callback.NOOP);
    }

    @Override
    public void close(CloseStatus closeStatus)
    {
        coreSession.close(closeStatus.getCode(), closeStatus.getPhrase(), Callback.NOOP);
    }

    @Override
    public void close(int statusCode, String reason)
    {
        coreSession.close(statusCode, reason, Callback.NOOP);
    }

    @Override
    public void close(int statusCode, String reason, WriteCallback callback)
    {
        coreSession.close(statusCode, reason, Callback.from(callback::writeSuccess, callback::writeFailed));
    }

    @Override
    public WebSocketBehavior getBehavior()
    {
        switch (coreSession.getBehavior())
        {
            case CLIENT:
                return WebSocketBehavior.CLIENT;
            case SERVER:
                return WebSocketBehavior.SERVER;
            default:
                return null;
        }
    }

    @Override
    public Duration getIdleTimeout()
    {
        return coreSession.getIdleTimeout();
    }

    @Override
    public int getInputBufferSize()
    {
        return coreSession.getInputBufferSize();
    }

    @Override
    public int getOutputBufferSize()
    {
        return coreSession.getOutputBufferSize();
    }

    @Override
    public long getMaxBinaryMessageSize()
    {
        return coreSession.getMaxBinaryMessageSize();
    }

    @Override
    public long getMaxTextMessageSize()
    {
        return coreSession.getMaxTextMessageSize();
    }

    @Override
    public long getMaxFrameSize()
    {
        return coreSession.getMaxFrameSize();
    }

    @Override
    public boolean isAutoFragment()
    {
        return coreSession.isAutoFragment();
    }

    @Override
    public void setIdleTimeout(Duration duration)
    {
        coreSession.setIdleTimeout(duration);
    }

    @Override
    public void setInputBufferSize(int size)
    {
        coreSession.setInputBufferSize(size);
    }

    @Override
    public void setOutputBufferSize(int size)
    {
        coreSession.setOutputBufferSize(size);
    }

    @Override
    public void setMaxBinaryMessageSize(long size)
    {
        coreSession.setMaxBinaryMessageSize(size);
    }

    @Override
    public void setMaxTextMessageSize(long size)
    {
        coreSession.setMaxTextMessageSize(size);
    }

    @Override
    public void setMaxFrameSize(long maxFrameSize)
    {
        coreSession.setMaxFrameSize(maxFrameSize);
    }

    @Override
    public void setAutoFragment(boolean autoFragment)
    {
        coreSession.setAutoFragment(autoFragment);
    }

    @Override
    public String getProtocolVersion()
    {
        return upgradeRequest.getProtocolVersion();
    }

    @Override
    public JettyWebSocketRemoteEndpoint getRemote()
    {
        return remoteEndpoint;
    }

    @Override
    public boolean isOpen()
    {
        return coreSession.isOutputOpen();
    }

    @Override
    public boolean isSecure()
    {
        return upgradeRequest.isSecure();
    }

    @Override
    public void disconnect()
    {
        coreSession.abort();
    }

    @Override
    public SocketAddress getLocalAddress()
    {
        return coreSession.getLocalAddress();
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        return coreSession.getRemoteAddress();
    }

    @Override
    public UpgradeRequest getUpgradeRequest()
    {
        return this.upgradeRequest;
    }

    @Override
    public UpgradeResponse getUpgradeResponse()
    {
        return this.upgradeResponse;
    }

    @Override
    public SuspendToken suspend()
    {
        frameHandler.suspend();
        return this;
    }

    @Override
    public void resume()
    {
        frameHandler.resume();
    }

    public CoreSession getCoreSession()
    {
        return coreSession;
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException
    {
        Dumpable.dumpObjects(out, indent, this, upgradeRequest, coreSession, remoteEndpoint, frameHandler);
    }

    @Override
    public String dumpSelf()
    {
        return String.format("%s@%x[behavior=%s,idleTimeout=%dms]",
            this.getClass().getSimpleName(), hashCode(),
            getPolicy().getBehavior(),
            getIdleTimeout().toMillis());
    }

    @Override
    public String toString()
    {
        return String.format("WebSocketSession[%s,to=%s,%s,%s]", getBehavior(), getIdleTimeout(), coreSession, frameHandler);
    }
}