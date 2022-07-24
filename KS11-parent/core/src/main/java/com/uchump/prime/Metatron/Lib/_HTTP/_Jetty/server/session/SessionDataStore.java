package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;
import java.util.Set;

/**
 * SessionDataStore
 *
 * A store for the data contained in a Session object. The store
 * would usually be persistent.
 */
public interface SessionDataStore extends SessionDataMap
{

    /**
     * Create a new SessionData
     *
     * @param id the id
     * @param created the timestamp when created
     * @param accessed the timestamp when accessed
     * @param lastAccessed the timestamp when last accessed
     * @param maxInactiveMs the max inactive time in milliseconds
     * @return a new SessionData object
     */
    public SessionData newSessionData(String id, long created, long accessed, long lastAccessed, long maxInactiveMs);

    /**
     * Called periodically, this method should search the data store
     * for sessions that have been expired for a 'reasonable' amount
     * of time.
     *
     * @param candidates if provided, these are keys of sessions that
     * the SessionDataStore thinks has expired and should be verified by the
     * SessionDataStore
     * @return set of session ids
     */
    public Set<String> getExpired(Set<String> candidates);

    /**
     * True if this type of datastore will passivate session objects
     *
     * @return true if this store can passivate sessions, false otherwise
     */
    public boolean isPassivating();

    /**
     * Test if data exists for a given session id.
     *
     * @param id Identity of session whose existence should be checked
     * @return true if valid, non-expired session exists
     * @throws Exception if problem checking existence with persistence layer
     */
    public boolean exists(String id) throws Exception;
}