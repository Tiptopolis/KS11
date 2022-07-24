package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 *
 */
public class EncodingHttpWriter extends HttpWriter
{
    final Writer _converter;

    public EncodingHttpWriter(HttpOutput out, String encoding)
    {
        super(out);
        try
        {
            _converter = new OutputStreamWriter(_bytes, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(char[] s, int offset, int length) throws IOException
    {
        HttpOutput out = _out;

        while (length > 0)
        {
            _bytes.reset();
            int chars = Math.min(length, MAX_OUTPUT_CHARS);

            _converter.write(s, offset, chars);
            _converter.flush();
            _bytes.writeTo(out);
            length -= chars;
            offset += chars;
        }
    }
}