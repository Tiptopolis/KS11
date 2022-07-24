package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common;



import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.AbstractLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Dumpable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Graceful;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.StatusCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketSessionListener;

public class SessionTracker extends AbstractLifeCycle implements WebSocketSessionListener, Graceful, Dumpable
{
    private final Set<Session> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private boolean isShutdown = false;

    public Collection<Session> getSessions()
    {
        return Set.copyOf(sessions);
    }

    @Override
    public void onWebSocketSessionOpened(Session session)
    { 
        sessions.add(session);
    }

    @Override
    public void onWebSocketSessionClosed(Session session)
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

                // SHUTDOWN is abnormal close status so it will hard close connection after sent.
                session.close(StatusCode.SHUTDOWN, "Container being shut down");
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