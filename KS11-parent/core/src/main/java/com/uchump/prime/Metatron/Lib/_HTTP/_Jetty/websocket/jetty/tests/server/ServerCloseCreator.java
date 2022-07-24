package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import java.util.concurrent.LinkedBlockingQueue;
import javax.servlet.ServletContext;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeResponse;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketCreator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.EchoSocket;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ServerCloseCreator implements JettyWebSocketCreator
{
    private final JettyWebSocketServletFactory serverFactory;
    private LinkedBlockingQueue<AbstractCloseEndpoint> createdSocketQueue = new LinkedBlockingQueue<>();

    public ServerCloseCreator(JettyWebSocketServletFactory serverFactory)
    {
        this.serverFactory = serverFactory;
    }

    @Override
    public Object createWebSocket(JettyServerUpgradeRequest req, JettyServerUpgradeResponse resp)
    {
        AbstractCloseEndpoint closeSocket = null;

        if (req.hasSubProtocol("fastclose"))
        {
            closeSocket = new FastCloseEndpoint();
            resp.setAcceptedSubProtocol("fastclose");
        }
        else if (req.hasSubProtocol("fastfail"))
        {
            closeSocket = new FastFailEndpoint();
            resp.setAcceptedSubProtocol("fastfail");
        }
        else if (req.hasSubProtocol("container"))
        {
            ServletContext context = req.getHttpServletRequest().getServletContext();
            WebSocketContainer container =
                (WebSocketContainer)context.getAttribute(WebSocketContainer.class.getName());
            closeSocket = new ContainerEndpoint(container);
            resp.setAcceptedSubProtocol("container");
        }
        else if (req.hasSubProtocol("closeInOnClose"))
        {
            closeSocket = new CloseInOnCloseEndpoint();
            resp.setAcceptedSubProtocol("closeInOnClose");
        }
        else if (req.hasSubProtocol("closeInOnCloseNewThread"))
        {
            closeSocket = new CloseInOnCloseEndpointNewThread();
            resp.setAcceptedSubProtocol("closeInOnCloseNewThread");
        }

        if (closeSocket != null)
        {
            createdSocketQueue.offer(closeSocket);
            return closeSocket;
        }
        else
        {
            return new EchoSocket();
        }
    }

    public AbstractCloseEndpoint pollLastCreated() throws InterruptedException
    {
        return createdSocketQueue.poll(5, SECONDS);
    }
}