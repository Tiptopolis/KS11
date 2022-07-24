package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.FutureCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.BatchMode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WriteCallback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.ProtocolException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JettyWebSocketRemoteEndpoint implements com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.RemoteEndpoint
{
    private static final Logger LOG = LoggerFactory.getLogger(JettyWebSocketRemoteEndpoint.class);

    private final CoreSession coreSession;
    private byte messageType = -1;
    private BatchMode batchMode;

    public JettyWebSocketRemoteEndpoint(CoreSession coreSession, BatchMode batchMode)
    {
        this.coreSession = Objects.requireNonNull(coreSession);
        this.batchMode = batchMode;
    }

    @Override
    public void sendString(String text) throws IOException
    {
        sendBlocking(new Frame(OpCode.TEXT).setPayload(text));
    }

    @Override
    public void sendString(String text, WriteCallback callback)
    {
        Callback cb = callback == null ? Callback.NOOP : Callback.from(callback::writeSuccess, callback::writeFailed);
        coreSession.sendFrame(new Frame(OpCode.TEXT).setPayload(text), cb, isBatch());
    }

    @Override
    public void sendBytes(ByteBuffer data) throws IOException
    {
        sendBlocking(new Frame(OpCode.BINARY).setPayload(data));
    }

    @Override
    public void sendBytes(ByteBuffer data, WriteCallback callback)
    {
        coreSession.sendFrame(new Frame(OpCode.BINARY).setPayload(data),
            Callback.from(callback::writeSuccess, callback::writeFailed),
            isBatch());
    }

    @Override
    public void sendPartialBytes(ByteBuffer fragment, boolean isLast) throws IOException
    {
        FutureCallback b = new FutureCallback();
        sendPartialBytes(fragment, isLast, b);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendPartialBytes(ByteBuffer fragment, boolean isLast, WriteCallback callback)
    {
        sendPartialBytes(fragment, isLast, Callback.from(callback::writeSuccess, callback::writeFailed));
    }

    private void sendPartialBytes(ByteBuffer fragment, boolean isLast, Callback callback)
    {
        Frame frame;
        switch (messageType)
        {
            case -1: // new message
                frame = new Frame(OpCode.BINARY);
                messageType = OpCode.BINARY;
                break;
            case OpCode.BINARY:
                frame = new Frame(OpCode.CONTINUATION);
                break;
            default:
                callback.failed(new ProtocolException("Attempt to send Partial Binary during active opcode " + messageType));
                return;
        }

        frame.setPayload(fragment);
        frame.setFin(isLast);

        coreSession.sendFrame(frame, callback, isBatch());

        if (isLast)
        {
            messageType = -1;
        }
    }

    @Override
    public void sendPartialString(String fragment, boolean isLast) throws IOException
    {
        FutureCallback b = new FutureCallback();
        sendPartialText(fragment, isLast, b);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    // FIXME: Remove the throws IOException from API for this method in the next major release.
    @Override
    public void sendPartialString(String fragment, boolean isLast, WriteCallback callback)
    {
        sendPartialText(fragment, isLast, Callback.from(callback::writeSuccess, callback::writeFailed));
    }

    @Override
    public void sendPing(ByteBuffer applicationData) throws IOException
    {
        sendBlocking(new Frame(OpCode.PING).setPayload(applicationData));
    }

    @Override
    public void sendPing(ByteBuffer applicationData, WriteCallback callback)
    {
        coreSession.sendFrame(new Frame(OpCode.PING).setPayload(applicationData),
            Callback.from(callback::writeSuccess, callback::writeFailed), false);
    }

    @Override
    public void sendPong(ByteBuffer applicationData) throws IOException
    {
        sendBlocking(new Frame(OpCode.PONG).setPayload(applicationData));
    }

    @Override
    public void sendPong(ByteBuffer applicationData, WriteCallback callback)
    {
        coreSession.sendFrame(new Frame(OpCode.PONG).setPayload(applicationData),
            Callback.from(callback::writeSuccess, callback::writeFailed), false);
    }

    private void sendPartialText(String fragment, boolean isLast, Callback callback)
    {
        Frame frame;
        switch (messageType)
        {
            case -1: // new message
                frame = new Frame(OpCode.TEXT);
                messageType = OpCode.TEXT;
                break;
            case OpCode.TEXT:
                frame = new Frame(OpCode.CONTINUATION);
                break;
            default:
                callback.failed(new ProtocolException("Attempt to send Partial Text during active opcode " + messageType));
                return;
        }

        frame.setPayload(BufferUtil.toBuffer(fragment, UTF_8));
        frame.setFin(isLast);

        coreSession.sendFrame(frame, callback, isBatch());

        if (isLast)
        {
            messageType = -1;
        }
    }

    private void sendBlocking(Frame frame) throws IOException
    {
        FutureCallback b = new FutureCallback();
        coreSession.sendFrame(frame, b, false);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.BatchMode getBatchMode()
    {
        return batchMode;
    }

    @Override
    public void setBatchMode(BatchMode mode)
    {
        batchMode = mode;
    }

    @Override
    public int getMaxOutgoingFrames()
    {
        return coreSession.getMaxOutgoingFrames();
    }

    @Override
    public void setMaxOutgoingFrames(int maxOutgoingFrames)
    {
        coreSession.setMaxOutgoingFrames(maxOutgoingFrames);
    }

    private boolean isBatch()
    {
        return BatchMode.ON == batchMode;
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        return coreSession.getRemoteAddress();
    }

    @Override
    public void flush() throws IOException
    {
        FutureCallback b = new FutureCallback();
        coreSession.flush(b);
        b.block(getBlockingTimeout(), TimeUnit.MILLISECONDS);
    }

    private long getBlockingTimeout()
    {
        long idleTimeout = coreSession.getIdleTimeout().toMillis();
        return (idleTimeout > 0) ? idleTimeout + 1000 : idleTimeout;
    }
}