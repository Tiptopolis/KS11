package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Do nothing OutputStream implementation
 */
class NoOpOutputStream extends OutputStream
{
    @Override
    public void write(byte[] b) throws IOException
    {
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
    }

    @Override
    public void flush() throws IOException
    {
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public void write(int b) throws IOException
    {
    }
}