package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ByteBufferPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.DeflaterPool;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.InflaterPool;

@ManagedObject("Abstract Extension")
public class AbstractExtension implements Extension
{
    private final Logger log;
    private CoreSession coreSession;
    private ByteBufferPool bufferPool;
    private ExtensionConfig config;
    private OutgoingFrames nextOutgoing;
    private IncomingFrames nextIncoming;
    private DeflaterPool deflaterPool;
    private InflaterPool inflaterPool;

    public AbstractExtension()
    {
        log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void init(ExtensionConfig config, WebSocketComponents components)
    {
        this.config = config;
        this.bufferPool = components.getBufferPool();
        this.deflaterPool = components.getDeflaterPool();
        this.inflaterPool = components.getInflaterPool();
    }

    @Override
    public void onFrame(Frame frame, Callback callback)
    {
        nextIncomingFrame(frame, callback);
    }

    @Override
    public void sendFrame(Frame frame, Callback callback, boolean batch)
    {
        nextOutgoingFrame(frame, callback, batch);
    }

    public ByteBufferPool getBufferPool()
    {
        return bufferPool;
    }

    @Override
    public ExtensionConfig getConfig()
    {
        return config;
    }

    public DeflaterPool getDeflaterPool()
    {
        return deflaterPool;
    }

    public InflaterPool getInflaterPool()
    {
        return inflaterPool;
    }

    @Override
    public String getName()
    {
        return config.getName();
    }

    @ManagedAttribute(name = "Next Incoming Frame Handler", readonly = true)
    public IncomingFrames getNextIncoming()
    {
        return nextIncoming;
    }

    @ManagedAttribute(name = "Next Outgoing Frame Handler", readonly = true)
    public OutgoingFrames getNextOutgoing()
    {
        return nextOutgoing;
    }

    /**
     * Used to indicate that the extension makes use of the RSV1 bit of the base websocket framing.
     * <p>
     * This is used to adjust validation during parsing, as well as a checkpoint against 2 or more extensions all simultaneously claiming ownership of RSV1.
     *
     * @return true if extension uses RSV1 for its own purposes.
     */
    @Override
    public boolean isRsv1User()
    {
        return false;
    }

    /**
     * Used to indicate that the extension makes use of the RSV2 bit of the base websocket framing.
     * <p>
     * This is used to adjust validation during parsing, as well as a checkpoint against 2 or more extensions all simultaneously claiming ownership of RSV2.
     *
     * @return true if extension uses RSV2 for its own purposes.
     */
    @Override
    public boolean isRsv2User()
    {
        return false;
    }

    /**
     * Used to indicate that the extension makes use of the RSV3 bit of the base websocket framing.
     * <p>
     * This is used to adjust validation during parsing, as well as a checkpoint against 2 or more extensions all simultaneously claiming ownership of RSV3.
     *
     * @return true if extension uses RSV3 for its own purposes.
     */
    @Override
    public boolean isRsv3User()
    {
        return false;
    }

    protected void nextIncomingFrame(Frame frame, Callback callback)
    {
        log.debug("nextIncomingFrame({})", frame);
        this.nextIncoming.onFrame(frame, callback);
    }

    protected void nextOutgoingFrame(Frame frame, Callback callback, boolean batch)
    {
        log.debug("nextOutgoingFrame({})", frame);
        this.nextOutgoing.sendFrame(frame, callback, batch);
    }

    @Override
    public void setNextIncomingFrames(IncomingFrames nextIncoming)
    {
        this.nextIncoming = nextIncoming;
    }

    @Override
    public void setNextOutgoingFrames(OutgoingFrames nextOutgoing)
    {
        this.nextOutgoing = nextOutgoing;
    }

    @Override
    public void setCoreSession(CoreSession coreSession)
    {
        this.coreSession = coreSession;
    }

    public CoreSession getCoreSession()
    {
        return coreSession;
    }

    protected Configuration getConfiguration()
    {
        return coreSession;
    }

    @Override
    public String toString()
    {
        return String.format("%s[%s]", this.getClass().getSimpleName(), config.getParameterizedName());
    }
}