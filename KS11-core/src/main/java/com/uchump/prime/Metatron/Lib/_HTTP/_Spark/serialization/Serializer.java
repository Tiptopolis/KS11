package com.uchump.prime.Metatron.Lib._HTTP._Spark.serialization;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class that serializers and writes the result to given output stream.
 *
 * @author alex
 */
public abstract class Serializer {

    private Serializer next;

    /**
     * Sets the next serializer in the chain.
     *
     * @param serializer the next serializer.
     */
    public void setNext(Serializer serializer) {
        this.next = serializer;
    }

    /**
     * Wraps {@link Serializer#process(java.io.OutputStream, Object)} and calls next serializer in chain.
     *
     * @param outputStream the output stream.
     * @param element      the element to process.
     * @throws IOException IOException in case of IO error.
     */
    public void processElement(OutputStream outputStream, Object element) throws IOException {
        if (canProcess(element)) {
            process(outputStream, element);
        } else {
            if (next != null) {
                this.next.processElement(outputStream, element);
            }
        }
    }

    /**
     * Checks if the serializer implementation can process the element type.
     *
     * @param element the element to process.
     * @return true if the serializer can process the provided element.
     */
    public abstract boolean canProcess(Object element);

    /**
     * Processes the provided element and serializes to output stream.
     *
     * @param outputStream the output stream.
     * @param element      the element.
     * @throws IOException In the case of IO error.
     */
    public abstract void process(OutputStream outputStream, Object element) throws IOException;
}