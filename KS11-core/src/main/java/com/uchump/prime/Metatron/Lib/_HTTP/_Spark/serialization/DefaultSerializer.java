package com.uchump.prime.Metatron.Lib._HTTP._Spark.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Serializer that writes the result of toString to output in UTF-8 encoding
 *
 * @author alex
 */
class DefaultSerializer extends Serializer {

    @Override
    public boolean canProcess(Object element) {
        return true;
    }

    @Override
    public void process(OutputStream outputStream, Object element) throws IOException {
        try {
            outputStream.write(element.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }

}