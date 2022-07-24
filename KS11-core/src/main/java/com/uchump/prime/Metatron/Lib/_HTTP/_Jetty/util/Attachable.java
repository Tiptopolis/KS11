package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;

/**
 * Abstract mechanism to support attachment of miscellaneous objects.
 */
public interface Attachable
{
    /**
     * @return the object attached to this instance
     * @see #setAttachment(Object)
     */
    Object getAttachment();

    /**
     * Attaches the given object to this stream for later retrieval.
     *
     * @param attachment the object to attach to this instance
     */
    void setAttachment(Object attachment);
}