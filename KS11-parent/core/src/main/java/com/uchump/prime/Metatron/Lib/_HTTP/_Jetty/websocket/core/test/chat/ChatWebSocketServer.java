package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.chat;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.ServletPathSpec;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.HttpConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.AbstractHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CloseStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.MessageHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiator;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketUpgradeHandler;

import static com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback.NOOP;

public class ChatWebSocketServer
{
    protected static final Logger LOG = LoggerFactory.getLogger(ChatWebSocketServer.class);

    private final Set<MessageHandler> members = new HashSet<>();

    private FrameHandler negotiate(WebSocketNegotiation negotiation)
    {
        // Finalize negotiations in API layer involves:
        //  + MAY mutate the policy
        //  + MAY replace the policy
        //  + MAY read request and set response headers
        //  + MAY reject with sendError semantics
        //  + MAY change/add/remove offered extensions
        //  + MUST pick subprotocol
        List<String> subprotocols = negotiation.getOfferedSubprotocols();
        if (!subprotocols.contains("chat"))
            return null;
        negotiation.setSubprotocol("chat");

        //  + MUST return the FrameHandler or null or exception?
        return new MessageHandler()
        {
            @Override
            public void onOpen(CoreSession coreSession, Callback callback)
            {
                LOG.debug("onOpen {}", coreSession);
                coreSession.setMaxTextMessageSize(2 * 1024);
                super.onOpen(coreSession, Callback.from(() ->
                {
                    members.add(this);
                    callback.succeeded();
                }, callback::failed));
            }

            @Override
            public void onText(String message, Callback callback)
            {
                for (MessageHandler handler : members)
                {
                    if (handler == this)
                        continue;
                    LOG.debug("Sending Message{} to {}", message, handler);
                    handler.sendText(message, NOOP, false);
                }

                callback.succeeded();
            }

            @Override
            public void onClosed(CloseStatus closeStatus, Callback callback)
            {
                LOG.debug("onClosed {}", closeStatus);
                super.onClosed(closeStatus, Callback.from(() -> members.remove(this), callback));
                members.remove(this);
            }
        };
    }

    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory());

        connector.setPort(8888);
        connector.setIdleTimeout(1000000);
        server.addConnector(connector);

        ContextHandler context = new ContextHandler("/");
        server.setHandler(context);

        ChatWebSocketServer chat = new ChatWebSocketServer();
        WebSocketComponents components = new WebSocketComponents();
        WebSocketUpgradeHandler upgradeHandler = new WebSocketUpgradeHandler(components);
        upgradeHandler.addMapping(new ServletPathSpec("/*"), WebSocketNegotiator.from(chat::negotiate));
        context.setHandler(upgradeHandler);

        upgradeHandler.setHandler(new AbstractHandler()
        {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                    throws IOException, ServletException
            {
                response.setStatus(200);
                response.setContentType("text/plain");
                response.getOutputStream().println("WebSocket Chat Server");
                baseRequest.setHandled(true);
            }
        });

        server.start();
        server.join();
    }
}