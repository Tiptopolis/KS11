package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.io.IOException;
import java.io.Writer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ByteArrayOutputStream2;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;


/**
 *
 */
public abstract class HttpWriter extends Writer
{
    public static final int MAX_OUTPUT_CHARS = 512; // TODO should this be configurable? super size is 1024

    final HttpOutput _out;
    final ByteArrayOutputStream2 _bytes;
    final char[] _chars;

    public HttpWriter(HttpOutput out)
    {
        _out = out;
        _chars = new char[MAX_OUTPUT_CHARS];
        _bytes = new ByteArrayOutputStream2(MAX_OUTPUT_CHARS);  // TODO should this be pooled - or do we just recycle the writer?
    }

    @Override
    public void close() throws IOException
    {
        _out.close();
    }

    public void complete(Callback callback)
    {
        _out.complete(callback);
    }

    @Override
    public void flush() throws IOException
    {
        _out.flush();
    }

    @Override
    public void write(String s, int offset, int length) throws IOException
    {
        while (length > MAX_OUTPUT_CHARS)
        {
            write(s, offset, MAX_OUTPUT_CHARS);
            offset += MAX_OUTPUT_CHARS;
            length -= MAX_OUTPUT_CHARS;
        }

        s.getChars(offset, offset + length, _chars, 0);
        write(_chars, 0, length);
    }

    @Override
    public void write(char[] s, int offset, int length) throws IOException
    {
        throw new AbstractMethodError();
    }
}