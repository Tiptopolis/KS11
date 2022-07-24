package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api;

import java.io.Closeable;

/**
 * {@link Connection} represent a connection to a {@link Destination} and allow applications to send
 * requests via {@link #send(Request, Response.CompleteListener)}.
 * <p>
 * {@link Connection}s are normally pooled by {@link Destination}s, but unpooled {@link Connection}s
 * may be created by applications that want to do their own connection management via
 * {@link Destination#newConnection(Promise)} and {@link Connection#close()}.
 */
public interface Connection extends Closeable
{
    /**
     * Sends a request with an associated response listener.
     * <p>
     * {@link Request#send(Response.CompleteListener)} will eventually call this method to send the request.
     * It is exposed to allow applications to send requests via unpooled connections.
     *
     * @param request the request to send
     * @param listener the response listener
     */
    void send(Request request, Response.CompleteListener listener);

    @Override
    void close();

    /**
     * @return whether this connection has been closed
     * @see #close()
     */
    boolean isClosed();
}