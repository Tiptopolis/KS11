package com.uchump.prime.Metatron.Lib._HTTP._Spark.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import spark.utils.IOUtils;

/**
 * Input stream serializer.
 *
 * @author alex
 */
class InputStreamSerializer extends Serializer {

    @Override
    public boolean canProcess(Object element) {
        return element instanceof InputStream;
    }

    @Override
    public void process(OutputStream outputStream, Object element)
            throws IOException {
        try (InputStream is = (InputStream) element) {
            IOUtils.copy(is, outputStream);
        }
    }

}