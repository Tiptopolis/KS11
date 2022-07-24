package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;

/**
 * Simple wrapper of a ByteBuffer as an OutputStream.
 * The buffer does not grow and this class will throw an
 * {@link java.nio.BufferOverflowException} if the buffer capacity is exceeded.
 */
public class ByteBufferOutputStream extends OutputStream
{
    final ByteBuffer _buffer;

    public ByteBufferOutputStream(ByteBuffer buffer)
    {
        _buffer = buffer;
    }

    public void close()
    {
    }

    public void flush()
    {
    }

    public void write(byte[] b)
    {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len)
    {
        BufferUtil.append(_buffer, b, off, len);
    }

    public void write(int b)
    {
        BufferUtil.append(_buffer, (byte)b);
    }
}