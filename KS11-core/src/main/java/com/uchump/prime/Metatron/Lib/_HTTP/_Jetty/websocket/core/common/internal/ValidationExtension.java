package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;

import static com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode.CONTINUATION;
import static com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode.TEXT;
import static com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OpCode.UNDEFINED;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.AbstractExtension;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.ProtocolException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.util.FrameValidation;

public class ValidationExtension extends AbstractExtension
{
    private static final Logger LOG = LoggerFactory.getLogger(ValidationExtension.class);

    private FrameSequence incomingSequence = null;
    private FrameSequence outgoingSequence = null;
    private boolean incomingFrameValidation = false;
    private boolean outgoingFrameValidation = false;
    private NullAppendable incomingUtf8Validation = null;
    private NullAppendable outgoingUtf8Validation = null;
    private byte continuedOutOpCode = UNDEFINED;
    private byte continuedInOpCode = UNDEFINED;

    @Override
    public String getName()
    {
        return "@validation";
    }

    @Override
    public void onFrame(Frame frame, Callback callback)
    {
        try
        {
            if (incomingSequence != null)
                incomingSequence.check(frame.getOpCode(), frame.isFin());

            if (incomingFrameValidation)
                FrameValidation.assertValidIncoming(frame, getCoreSession());

            if (incomingUtf8Validation != null)
                validateUTF8(frame, incomingUtf8Validation, continuedInOpCode);

            continuedInOpCode = recordLastOpCode(frame, continuedInOpCode);
            nextIncomingFrame(frame, callback);
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
    }

    @Override
    public void sendFrame(Frame frame, Callback callback, boolean batch)
    {
        try
        {
            if (outgoingSequence != null)
                outgoingSequence.check(frame.getOpCode(), frame.isFin());

            if (outgoingFrameValidation)
                FrameValidation.assertValidOutgoing(frame, getCoreSession());

            if (outgoingUtf8Validation != null)
                validateUTF8(frame, outgoingUtf8Validation, continuedOutOpCode);

            continuedOutOpCode = recordLastOpCode(frame, continuedOutOpCode);
            nextOutgoingFrame(frame, callback, batch);
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
    }

    @Override
    public void init(ExtensionConfig config, WebSocketComponents components)
    {
        super.init(config, components);

        Map<String, String> parameters = config.getParameters();

        if (parameters.containsKey("outgoing-sequence"))
            outgoingSequence = new FrameSequence();

        if (parameters.containsKey("incoming-sequence"))
            incomingSequence = new FrameSequence();

        if (parameters.containsKey("incoming-frame"))
            incomingFrameValidation = true;

        if (parameters.containsKey("outgoing-frame"))
            outgoingFrameValidation = true;

        if (parameters.containsKey("incoming-utf8"))
            incomingUtf8Validation = new NullAppendable();

        if (parameters.containsKey("outgoing-utf8"))
            outgoingUtf8Validation = new NullAppendable();
    }

    private void validateUTF8(Frame frame, NullAppendable appendable, byte continuedOpCode)
    {
        //TODO this relies on sequencing being set

        if (frame.isControlFrame())
        {
            //todo validate utf8 of control frames

        }
        else
        {
            if (frame.getOpCode() == TEXT || continuedOpCode == TEXT)
            {
                if (frame.hasPayload())
                    appendable.append(frame.getPayload().slice());

                if (frame.isFin())
                    appendable.checkState();
            }
        }
    }

    public byte recordLastOpCode(Frame frame, byte prevOpcode) throws ProtocolException
    {
        byte opcode = prevOpcode;
        boolean fin = frame.isFin();

        if (fin)
            opcode = UNDEFINED;
        else if (opcode != CONTINUATION)
            opcode = frame.getOpCode();

        return opcode;
    }
}