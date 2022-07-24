package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.FutureCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.TextUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JavaxWebSocketBasicRemote extends JavaxWebSocketRemoteEndpoint implements RemoteEndpoint.Basic
{
    private static final Logger LOG = LoggerFactory.getLogger(JavaxWebSocketBasicRemote.class);

    protected JavaxWebSocketBasicRemote(JavaxWebSocketSession session, CoreSession coreSession)
    {
        super(session, coreSession);
    }

    @Override
    public OutputStream getSendStream() throws IOException
    {
        return newMessageOutputStream();
    }

    @Override
    public Writer getSendWriter() throws IOException
    {
        return newMessageWriter();
    }

    @Override
    public void sendBinary(ByteBuffer data) throws IOException
    {
        assertMessageNotNull(data);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendBinary({})", BufferUtil.toDetailString(data));
        }

        FutureCallback b = new FutureCallback();
        sendFrame(new Frame(OpCode.BINARY).setPayload(data), b, false);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendBinary(ByteBuffer partialByte, boolean isLast) throws IOException
    {
        assertMessageNotNull(partialByte);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendBinary({},{})", BufferUtil.toDetailString(partialByte), isLast);
        }

        Frame frame;
        switch (messageType)
        {
            case -1:
                // New message!
                frame = new Frame(OpCode.BINARY);
                break;
            case OpCode.TEXT:
                throw new IllegalStateException("Cannot send a partial BINARY message: TEXT message in progress");
            case OpCode.BINARY:
                frame = new Frame(OpCode.CONTINUATION);
                break;
            default:
                throw new IllegalStateException("Cannot send a partial BINARY message: unrecognized active message type " + OpCode.name(messageType));
        }

        frame.setPayload(partialByte);
        frame.setFin(isLast);
        FutureCallback b = new FutureCallback();
        sendFrame(frame, b, false);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendObject(Object data) throws IOException, EncodeException
    {
        FutureCallback b = new FutureCallback();
        super.sendObject(data, b);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendText(String text) throws IOException
    {
        assertMessageNotNull(text);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendText({})", TextUtils.hint(text));
        }

        FutureCallback b = new FutureCallback();
        sendFrame(new Frame(OpCode.TEXT).setPayload(text), b, false);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendText(String partialMessage, boolean isLast) throws IOException
    {
        assertMessageNotNull(partialMessage);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("sendText({},{})", TextUtils.hint(partialMessage), isLast);
        }

        Frame frame;
        switch (messageType)
        {
            case -1:
                // New message!
                frame = new Frame(OpCode.TEXT);
                break;
            case OpCode.TEXT:
                frame = new Frame(OpCode.CONTINUATION);
                break;
            case OpCode.BINARY:
                throw new IllegalStateException("Cannot send a partial TEXT message: BINARY message in progress");
            default:
                throw new IllegalStateException("Cannot send a partial TEXT message: unrecognized active message type " + OpCode.name(messageType));
        }

        frame.setPayload(BufferUtil.toBuffer(partialMessage, UTF_8));
        frame.setFin(isLast);
        FutureCallback b = new FutureCallback();
        sendFrame(frame, b, false);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    private long getBlockingTimeout()
    {
        long idleTimeout = getIdleTimeout();
        return (idleTimeout > 0) ? idleTimeout + 1000 : idleTimeout;
    }
}