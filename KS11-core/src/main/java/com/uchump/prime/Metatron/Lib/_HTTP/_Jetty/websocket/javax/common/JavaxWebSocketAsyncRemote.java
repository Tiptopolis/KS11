package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.FutureCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.MessageOutputStream;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.MessageWriter;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.TextUtils;

public class JavaxWebSocketAsyncRemote extends JavaxWebSocketRemoteEndpoint implements javax.websocket.RemoteEndpoint.Async
{
    static final Logger LOG = LoggerFactory.getLogger(JavaxWebSocketAsyncRemote.class);

    protected JavaxWebSocketAsyncRemote(JavaxWebSocketSession session, CoreSession coreSession)
    {
        super(session, coreSession);
    }

    @Override
    public long getSendTimeout()
    {
        return getWriteTimeout();
    }

    @Override
    public void setSendTimeout(long timeoutmillis)
    {
        setWriteTimeout(timeoutmillis);
    }

    @Override
    public Future<Void> sendBinary(ByteBuffer data)
    {
        assertMessageNotNull(data);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendBinary({})", BufferUtil.toDetailString(data));
        }

        FutureCallback future = new FutureCallback();
        sendFrame(new Frame(OpCode.BINARY).setPayload(data), future, batch);
        return future;
    }

    @Override
    public void sendBinary(ByteBuffer data, javax.websocket.SendHandler handler)
    {
        assertMessageNotNull(data);
        assertSendHandlerNotNull(handler);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendBinary({},{})", BufferUtil.toDetailString(data), handler);
        }
        sendFrame(new Frame(OpCode.BINARY).setPayload(data), new SendHandlerCallback(handler), batch);
    }

    @Override
    public Future<Void> sendObject(Object data)
    {
        FutureCallback future = new FutureCallback();
        try
        {
            sendObject(data, future);
        }
        catch (Throwable t)
        {
            future.failed(t);
        }
        return future;
    }

    @SuppressWarnings(
        {"rawtypes", "unchecked"})
    @Override
    public void sendObject(Object data, SendHandler handler)
    {
        assertMessageNotNull(data);
        assertSendHandlerNotNull(handler);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendObject({},{})", data, handler);
        }

        Encoder encoder = session.getEncoders().getInstanceFor(data.getClass());
        if (encoder == null)
        {
            throw new IllegalArgumentException("No encoder for type: " + data.getClass());
        }

        if (encoder instanceof Encoder.Text)
        {
            Encoder.Text etxt = (Encoder.Text)encoder;
            try
            {
                String msg = etxt.encode(data);
                sendText(msg, handler);
                return;
            }
            catch (EncodeException e)
            {
                handler.onResult(new SendResult(e));
            }
        }
        else if (encoder instanceof Encoder.TextStream)
        {
            Encoder.TextStream etxt = (Encoder.TextStream)encoder;
            SendHandlerCallback callback = new SendHandlerCallback(handler);
            try (MessageWriter writer = newMessageWriter())
            {
                writer.setCallback(callback);
                etxt.encode(data, writer);
                return;
            }
            catch (EncodeException | IOException e)
            {
                handler.onResult(new SendResult(e));
            }
        }
        else if (encoder instanceof Encoder.Binary)
        {
            Encoder.Binary ebin = (Encoder.Binary)encoder;
            try
            {
                ByteBuffer buf = ebin.encode(data);
                sendBinary(buf, handler);
                return;
            }
            catch (EncodeException e)
            {
                handler.onResult(new SendResult(e));
            }
        }
        else if (encoder instanceof Encoder.BinaryStream)
        {
            Encoder.BinaryStream ebin = (Encoder.BinaryStream)encoder;
            SendHandlerCallback callback = new SendHandlerCallback(handler);
            try (MessageOutputStream out = newMessageOutputStream())
            {
                out.setCallback(callback);
                ebin.encode(data, out);
                return;
            }
            catch (EncodeException | IOException e)
            {
                handler.onResult(new SendResult(e));
            }
        }

        throw new IllegalArgumentException("Unknown encoder type: " + encoder);
    }

    @Override
    public Future<Void> sendText(String text)
    {
        assertMessageNotNull(text);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendText({})", TextUtils.hint(text));
        }
        FutureCallback future = new FutureCallback();
        sendFrame(new Frame(OpCode.TEXT).setPayload(text), future, batch);
        return future;
    }

    @Override
    public void sendText(String text, SendHandler handler)
    {
        assertMessageNotNull(text);
        assertSendHandlerNotNull(handler);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendText({},{})", TextUtils.hint(text), handler);
        }
        sendFrame(new Frame(OpCode.TEXT).setPayload(text), new SendHandlerCallback(handler), batch);
    }
}