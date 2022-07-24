package com.uchump.prime.Metatron.Lib._HTTP._Spark.serialization;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Chain of serializers for the output.
 */
public final class SerializerChain {

    private Serializer root;

    /**
     * Constructs a serializer chain.
     */
    public SerializerChain() {

        DefaultSerializer defaultSerializer = new DefaultSerializer();

        InputStreamSerializer inputStreamSerializer = new InputStreamSerializer();
        inputStreamSerializer.setNext(defaultSerializer);

        BytesSerializer bytesSerializer = new BytesSerializer();
        bytesSerializer.setNext(inputStreamSerializer);

        this.root = bytesSerializer;
    }

    /**
     * Process the output.
     *
     * @param outputStream the output stream to write to.
     * @param element      the element to serialize.
     * @throws IOException in the case of IO error.
     */
    public void process(OutputStream outputStream, Object element) throws IOException {
        this.root.processElement(outputStream, element);
    }

}