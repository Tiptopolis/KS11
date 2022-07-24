package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.server;

import java.util.Collection;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketContainer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WriteCallback;

/**
 * On Message, return container information
 */
public class ContainerEndpoint extends AbstractCloseEndpoint
{
    private final WebSocketContainer container;
    private Session session;

    public ContainerEndpoint(WebSocketContainer container)
    {
        super();
        this.container = container;
    }

    @Override
    public void onWebSocketText(String message)
    {
        log.debug("onWebSocketText({})", message);
        if (message.equalsIgnoreCase("openSessions"))
        {
            Collection<Session> sessions = container.getOpenSessions();

            StringBuilder ret = new StringBuilder();
            ret.append("openSessions.size=").append(sessions.size()).append('\n');
            int idx = 0;
            for (Session sess : sessions)
            {
                ret.append('[').append(idx++).append("] ").append(sess.toString()).append('\n');
            }
            session.getRemote().sendString(ret.toString(), WriteCallback.NOOP);
        }
        session.close(StatusCode.NORMAL, "ContainerEndpoint");
    }

    @Override
    public void onWebSocketConnect(Session sess)
    {
        log.debug("onWebSocketConnect({})", sess);
        this.session = sess;
    }
}