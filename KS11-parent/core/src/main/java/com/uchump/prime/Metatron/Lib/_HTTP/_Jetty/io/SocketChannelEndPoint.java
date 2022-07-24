package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.Scheduler;

/**
 * <p>An {@link EndPoint} implementation based on {@link SocketChannel}.</p>
 */
public class SocketChannelEndPoint extends SelectableChannelEndPoint
{
    private static final Logger LOG = LoggerFactory.getLogger(SocketChannelEndPoint.class);

    public SocketChannelEndPoint(SocketChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler)
    {
        super(scheduler, channel, selector, key);
    }

    @Override
    public SocketChannel getChannel()
    {
        return (SocketChannel)super.getChannel();
    }

    @Override
    public SocketAddress getRemoteSocketAddress()
    {
        try
        {
            return getChannel().getRemoteAddress();
        }
        catch (Throwable x)
        {
            LOG.trace("Could not retrieve remote socket address", x);
            return null;
        }
    }

    @Override
    protected void doShutdownOutput()
    {
        try
        {
            getChannel().shutdownOutput();
        }
        catch (Throwable x)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Could not shutdown output for {}", getChannel(), x);
        }
    }

    @Override
    public int fill(ByteBuffer buffer) throws IOException
    {
        if (isInputShutdown())
            return -1;

        int pos = BufferUtil.flipToFill(buffer);
        int filled;
        try
        {
            filled = getChannel().read(buffer);
            if (filled > 0)
                notIdle();
            else if (filled == -1)
                shutdownInput();
        }
        catch (IOException e)
        {
            LOG.debug("Unable to shutdown output", e);
            shutdownInput();
            filled = -1;
        }
        finally
        {
            BufferUtil.flipToFlush(buffer, pos);
        }
        if (LOG.isDebugEnabled())
            LOG.debug("filled {} {}", filled, BufferUtil.toDetailString(buffer));
        return filled;
    }

    @Override
    public boolean flush(ByteBuffer... buffers) throws IOException
    {
        long flushed;
        try
        {
            flushed = getChannel().write(buffers);
            if (LOG.isDebugEnabled())
                LOG.debug("flushed {} {}", flushed, this);
        }
        catch (IOException e)
        {
            throw new EofException(e);
        }

        if (flushed > 0)
            notIdle();

        for (ByteBuffer b : buffers)
        {
            if (!BufferUtil.isEmpty(b))
                return false;
        }

        return true;
    }
}