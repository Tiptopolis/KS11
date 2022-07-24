package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * Wrap a Writer as an OutputStream.
 * When all you have is a Writer and only an OutputStream will do.
 * Try not to use this as it indicates that your design is a dogs
 * breakfast (JSP made me write it).
 */
public class WriterOutputStream extends OutputStream
{
    protected final Writer _writer;
    protected final Charset _encoding;

    public WriterOutputStream(Writer writer, String encoding)
    {
        _writer = writer;
        _encoding = encoding == null ? null : Charset.forName(encoding);
    }

    public WriterOutputStream(Writer writer)
    {
        _writer = writer;
        _encoding = null;
    }

    @Override
    public void close()
        throws IOException
    {
        _writer.close();
    }

    @Override
    public void flush()
        throws IOException
    {
        _writer.flush();
    }

    @Override
    public void write(byte[] b)
        throws IOException
    {
        if (_encoding == null)
            _writer.write(new String(b));
        else
            _writer.write(new String(b, _encoding));
    }

    @Override
    public void write(byte[] b, int off, int len)
        throws IOException
    {
        if (_encoding == null)
            _writer.write(new String(b, off, len));
        else
            _writer.write(new String(b, off, len, _encoding));
    }

    @Override
    public void write(int b)
        throws IOException
    {
        write(new byte[]{(byte)b});
    }
}