package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.CloseReason;
import javax.websocket.Session;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.AbstractLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Dumpable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Graceful;

public class SessionTracker extends AbstractLifeCycle implements JavaxWebSocketSessionListener, Graceful, Dumpable
{
    private final Set<JavaxWebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private boolean isShutdown = false;

    public Set<Session> getSessions()
    {
        return Set.copyOf(sessions);
    }

    @Override
    public void onJavaxWebSocketSessionOpened(JavaxWebSocketSession session)
    {
        sessions.add(session);
    }

    @Override
    public void onJavaxWebSocketSessionClosed(JavaxWebSocketSession session)
    {
        sessions.remove(session);
    }

    @Override
    protected void doStart() throws Exception
    {
        isShutdown = false;
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception
    {
        sessions.clear();
        super.doStop();
    }

    @Override
    public CompletableFuture<Void> shutdown()
    {
        isShutdown = true;
        return Graceful.shutdown(() ->
        {
            for (Session session : sessions)
            {
                if (Thread.interrupted())
                    break;

                // GOING_AWAY is abnormal close status so it will hard close connection after sent.
                session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Container being shut down"));
            }
        });
    }

    @Override
    public boolean isShutdown()
    {
        return isShutdown;
    }

    @ManagedAttribute("Total number of active WebSocket Sessions")
    public int getNumSessions()
    {
        return sessions.size();
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException
    {
        Dumpable.dumpObjects(out, indent, this, sessions);
    }
}