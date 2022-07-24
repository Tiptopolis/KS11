package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;

import java.util.Collections;
import java.util.Set;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;


/**
 * NullSessionDataStore
 *
 * Does not actually store anything, useful for testing.
 */
@ManagedObject
public class NullSessionDataStore extends AbstractSessionDataStore
{
    @Override
    public SessionData doLoad(String id) throws Exception
    {
        return null;
    }

    @Override
    public SessionData newSessionData(String id, long created, long accessed, long lastAccessed, long maxInactiveMs)
    {
        return new SessionData(id, _context.getCanonicalContextPath(), _context.getVhost(), created, accessed, lastAccessed, maxInactiveMs);
    }

    @Override
    public boolean delete(String id) throws Exception
    {
        return true;
    }

    @Override
    public void doStore(String id, SessionData data, long lastSaveTime) throws Exception
    {
        //noop
    }

    @Override
    public Set<String> doCheckExpired(Set<String> candidates, long time)
    {
        return candidates; //whatever is suggested we accept
    }

    @Override
    public Set<String> doGetExpired(long timeLimit)
    {
        return Collections.emptySet();
    }
    
    /** 
     * @see org.eclipse.jetty.server.session.SessionDataStore#isPassivating()
     */
    @ManagedAttribute(value = "does this store serialize sessions", readonly = true)
    @Override
    public boolean isPassivating()
    {
        return false;
    }

    @Override
    public boolean doExists(String id)
    {
        return false;
    }

    @Override
    public void doCleanOrphans(long timeLimit)
    {
        //noop
    }
}