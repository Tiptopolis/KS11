package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

/**
 * The compliance level for parsing <code>multiPart/form-data</code>
 */
public enum MultiPartFormDataCompliance
{
    /**
     * Legacy <code>multiPart/form-data</code> parsing which is slow but forgiving.
     * It will accept non-compliant preambles and inconsistent line termination.
     *
     * @see org.eclipse.jetty.server.MultiPartInputStreamParser
     */
    LEGACY,
    /**
     * RFC7578 compliant parsing that is a fast but strict parser.
     *
     * @see org.eclipse.jetty.server.MultiPartFormInputStream
     */
    RFC7578
}