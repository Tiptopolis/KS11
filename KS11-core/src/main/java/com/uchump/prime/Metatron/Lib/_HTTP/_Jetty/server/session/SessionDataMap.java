package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.LifeCycle;

/**
 * SessionDataMap
 *
 * A map style access to SessionData keyed by the session id.
 */
public interface SessionDataMap extends LifeCycle
{
    /**
     * Initialize this data map for the
     * given context. A SessionDataMap can only
     * be used by one context(/session manager).
     *
     * @param context context associated
     * @throws Exception if unable to initialize the
     */
    void initialize(SessionContext context) throws Exception;

    /**
     * Read in session data.
     *
     * @param id identity of session to load
     * @return the SessionData matching the id
     * @throws Exception if unable to load session data
     */
    public SessionData load(String id) throws Exception;

    /**
     * Store the session data.
     *
     * @param id identity of session to store
     * @param data info of session to store
     * @throws Exception if unable to write session data
     */
    public void store(String id, SessionData data) throws Exception;

    /**
     * Delete session data
     *
     * @param id identity of session to delete
     * @return true if the session was deleted
     * @throws Exception if unable to delete session data
     */
    public boolean delete(String id) throws Exception;
}