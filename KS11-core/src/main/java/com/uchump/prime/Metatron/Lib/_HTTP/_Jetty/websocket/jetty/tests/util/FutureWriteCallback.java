package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.util;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.FutureCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WriteCallback;

/**
 * Allows events to a {@link WriteCallback} to drive a {@link Future} for the user.
 */
public class FutureWriteCallback extends FutureCallback implements WriteCallback
{
    private static final Logger LOG = LoggerFactory.getLogger(FutureWriteCallback.class);

    @Override
    public void writeFailed(Throwable cause)
    {
        if (LOG.isDebugEnabled())
            LOG.debug(".writeFailed", cause);
        failed(cause);
    }

    @Override
    public void writeSuccess()
    {
        if (LOG.isDebugEnabled())
            LOG.debug(".writeSuccess");
        succeeded();
    }
}