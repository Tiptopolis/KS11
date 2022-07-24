package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.resource;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for writing sections (ranges) of a single resource (SeekableByteChannel, Resource, etc) to an outputStream.
 */
public interface RangeWriter extends Closeable
{
    /**
     * Write the specific range (start, size) to the outputStream.
     *
     * @param outputStream the stream to write to
     * @param skipTo the offset / skip-to / seek-to / position in the resource to start the write from
     * @param length the size of the section to write
     */
    void writeTo(OutputStream outputStream, long skipTo, long length) throws IOException;
}