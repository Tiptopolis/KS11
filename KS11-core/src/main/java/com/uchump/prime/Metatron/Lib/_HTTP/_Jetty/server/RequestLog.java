package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.io.IOException;

/**
 * A <code>RequestLog</code> can be attached to a {@link org.eclipse.jetty.server.handler.RequestLogHandler} to enable
 * logging of requests/responses.
 *
 * @see RequestLogHandler#setRequestLog(RequestLog)
 * @see Server#setRequestLog(RequestLog)
 */
public interface RequestLog
{
    /**
     * @param request The request to log.
     * @param response The response to log.  Note that for some requests
     * the response instance may not have been fully populated (Eg 400 bad request
     * responses are sent without a servlet response object).  Thus for basic
     * log information it is best to consult {@link Response#getCommittedMetaData()}
     * and {@link Response#getHttpChannel()} directly.
     */
    void log(Request request, Response response);

    /**
     * Writes the generated log string to a log sink
     */
    interface Writer
    {
        void write(String requestEntry) throws IOException;
    }

    class Collection implements RequestLog
    {
        private final RequestLog[] _logs;

        public Collection(RequestLog... logs)
        {
            _logs = logs;
        }

        @Override
        public void log(Request request, Response response)
        {
            for (RequestLog log : _logs)
            {
                log.log(request, response);
            }
        }
    }
}