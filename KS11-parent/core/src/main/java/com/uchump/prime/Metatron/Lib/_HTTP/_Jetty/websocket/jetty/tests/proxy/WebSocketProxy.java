package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.proxy;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.StringUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketConnectionListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPartialListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketPingPongListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.exceptions.WebSocketException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.ClientUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.client.WebSocketClient;

public class WebSocketProxy
{
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketProxy.class);

    private final WebSocketClient client;
    private final URI serverUri;
    private final ClientToProxy clientToProxy = new ClientToProxy();
    private final ProxyToServer proxyToServer = new ProxyToServer();

    public WebSocketProxy(WebSocketClient webSocketClient, URI serverUri)
    {
        this.client = webSocketClient;
        this.serverUri = serverUri;
    }

    public WebSocketConnectionListener getWebSocketConnectionListener()
    {
        return clientToProxy;
    }

    public boolean awaitClose(long timeout)
    {
        try
        {
            if (!clientToProxy.closeLatch.await(timeout, TimeUnit.MILLISECONDS))
                return false;
            if (proxyToServer.getSession() == null)
                return true;
            return proxyToServer.closeLatch.await(timeout, TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public class ClientToProxy implements WebSocketPartialListener, WebSocketPingPongListener
    {
        private volatile Session session;
        private final CountDownLatch closeLatch = new CountDownLatch(1);

        public Session getSession()
        {
            return session;
        }

        public void fail(Throwable failure)
        {
            session.close(StatusCode.SERVER_ERROR, failure.getMessage());
        }

        @Override
        public void onWebSocketConnect(Session session)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketConnect({})", getClass().getSimpleName(), session);

            Future<Session> connect = null;
            try
            {
                this.session = session;
                ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
                upgradeRequest.setSubProtocols(session.getUpgradeRequest().getSubProtocols());
                upgradeRequest.setExtensions(session.getUpgradeRequest().getExtensions());
                connect = client.connect(proxyToServer, serverUri, upgradeRequest);

                //This is blocking as we really want the client to be connected before receiving any messages.
                connect.get();
            }
            catch (Exception e)
            {
                if (connect != null)
                    connect.cancel(true);
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketPartialBinary(ByteBuffer payload, boolean fin)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketPartialBinary({}, {})", getClass().getSimpleName(), BufferUtil.toDetailString(payload), fin);

            try
            {
                proxyToServer.getSession().getRemote().sendPartialBytes(payload, fin);
            }
            catch (Exception e)
            {
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketPartialText(String payload, boolean fin)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketPartialText({}, {})", getClass().getSimpleName(), StringUtil.truncate(payload, 100), fin);

            try
            {
                proxyToServer.getSession().getRemote().sendPartialString(payload, fin);
            }
            catch (Exception e)
            {
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketPing(ByteBuffer payload)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketPing({})", getClass().getSimpleName(), BufferUtil.toDetailString(payload));

            try
            {
                proxyToServer.getSession().getRemote().sendPing(payload);
            }
            catch (Exception e)
            {
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketPong(ByteBuffer payload)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketPong({})", getClass().getSimpleName(), BufferUtil.toDetailString(payload));

            try
            {
                proxyToServer.getSession().getRemote().sendPong(payload);
            }
            catch (Exception e)
            {
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketError(Throwable cause)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketError()", getClass().getSimpleName(), cause);

            proxyToServer.fail(cause);
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketClose({} {})", getClass().getSimpleName(), statusCode, reason);

            // Session may be null if connection to the server failed.
            Session session = proxyToServer.getSession();
            if (session != null)
                session.close(statusCode, reason);
            closeLatch.countDown();
        }
    }

    public class ProxyToServer implements WebSocketPartialListener, WebSocketPingPongListener
    {
        private volatile Session session;
        private final CountDownLatch closeLatch = new CountDownLatch(1);

        public Session getSession()
        {
            return session;
        }

        public void fail(Throwable failure)
        {
            // Only ProxyToServer can be failed before it is opened (if ClientToProxy fails before the connect completes).
            Session session = this.session;
            if (session != null)
                session.close(StatusCode.SERVER_ERROR, failure.getMessage());
        }

        @Override
        public void onWebSocketConnect(Session session)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketConnect({})", getClass().getSimpleName(), session);

            this.session = session;
        }

        @Override
        public void onWebSocketPartialBinary(ByteBuffer payload, boolean fin)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketPartialBinary({}, {})", getClass().getSimpleName(), BufferUtil.toDetailString(payload), fin);

            try
            {
                clientToProxy.getSession().getRemote().sendPartialBytes(payload, fin);
            }
            catch (Exception e)
            {
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketPartialText(String payload, boolean fin)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketPartialText({}, {})", getClass().getSimpleName(), StringUtil.truncate(payload, 100), fin);

            try
            {
                clientToProxy.getSession().getRemote().sendPartialString(payload, fin);
            }
            catch (Exception e)
            {
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketPing(ByteBuffer payload)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketPing({})", getClass().getSimpleName(), BufferUtil.toDetailString(payload));

            try
            {
                clientToProxy.getSession().getRemote().sendPing(payload);
            }
            catch (Exception e)
            {
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketPong(ByteBuffer payload)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketPong({})", getClass().getSimpleName(), BufferUtil.toDetailString(payload));

            try
            {
                clientToProxy.getSession().getRemote().sendPong(payload);
            }
            catch (Exception e)
            {
                throw new WebSocketException(e);
            }
        }

        @Override
        public void onWebSocketError(Throwable cause)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketError()", getClass().getSimpleName(), cause);

            clientToProxy.fail(cause);
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("{} onWebSocketClose({} {})", getClass().getSimpleName(), statusCode, reason);

            clientToProxy.getSession().close(statusCode, reason);
            closeLatch.countDown();
        }
    }
}