package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Collection of common asserts for Strings.
 */
public final class StringAssert
{
    private StringAssert()
    {
        /* prevent instantiation */
    }

    /**
     * Asserts that string (<code>haystack</code>) contains specified text (
     * <code>needle</code>).
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param needle the text to search for
     */
    public static void assertContains(String msg, String haystack, String needle)
    {
        assertNotNull(haystack, msg + ": haystack should not be null");
        assertNotNull(needle, msg + ": needle should not be null");

        int idx = haystack.indexOf(needle);
        if (idx == (-1))
        {
            StringBuffer buf = new StringBuffer();
            buf.append(msg).append(": Unable to find \"").append(needle).append("\" in \"");
            buf.append(haystack).append('\"');
            System.err.println(buf);
            fail(buf.toString());
        }
    }

    /**
     * Asserts that string (<code>haystack</code>) contains specified text (
     * <code>needle</code>), starting at offset (in <code>haystack</code>).
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param needle the text to search for
     * @param offset the offset in (haystack) to perform search from
     */
    public static void assertContains(String msg, String haystack, String needle, int offset)
    {
        assertNotNull(haystack, msg + ": haystack should not be null");
        assertNotNull(needle, msg + ": needle should not be null");

        int idx = haystack.indexOf(needle, offset);
        if (idx == (-1))
        {
            StringBuffer buf = new StringBuffer();
            buf.append(msg).append(": Unable to find \"").append(needle).append("\" in \"");
            buf.append(haystack.substring(offset)).append('\"');
            System.err.println(buf);
            fail(buf.toString());
        }
    }

    /**
     * Asserts that the list of String lines contains the same lines (without a regard for the order of those lines)
     *
     * @param msg the assertion message
     * @param linesExpected the list of expected lines
     * @param linesActual the list of actual lines
     */
    public static void assertContainsSame(String msg, List<String> linesExpected, List<String> linesActual)
    {
        assertEquals(linesExpected.size(), linesActual.size(), msg + " line count");

        for (String expected : linesExpected)
        {
            assertTrue(linesActual.contains(expected), msg + ": expecting to see line <" + expected + ">");
        }
    }

    /**
     * Asserts that string (<code>haystack</code>) does <u>not</u> contain
     * specified text (<code>needle</code>).
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param needle the text to search for
     */
    public static void assertNotContains(String msg, String haystack, String needle)
    {
        assertNotNull(haystack, msg + ": haystack should not be null");
        assertNotNull(needle, msg + ": needle should not be null");

        int idx = haystack.indexOf(needle);
        if (idx != (-1))
        {
            StringBuffer buf = new StringBuffer();
            buf.append(msg).append(": Should not have found \"").append(needle).append("\" at offset ");
            buf.append(idx).append(" in \"").append(haystack).append('\"');
            System.err.println(buf);
            fail(buf.toString());
        }
    }

    /**
     * Asserts that string (<code>haystack</code>) does <u>not</u> contain
     * specified text (<code>needle</code>), starting at offset (in
     * <code>haystack</code>).
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param needle the text to search for
     * @param offset the offset in (haystack) to perform search from
     */
    public static void assertNotContains(String msg, String haystack, String needle, int offset)
    {
        assertNotNull(haystack, msg + ": haystack should not be null");
        assertNotNull(needle, msg + ": needle should not be null");

        int idx = haystack.indexOf(needle, offset);
        if (idx != (-1))
        {
            StringBuffer buf = new StringBuffer();
            buf.append(msg).append(": Should not have found \"").append(needle).append("\" at offset ");
            buf.append(idx).append(" in \"").append(haystack.substring(offset)).append('\"');
            System.err.println(buf);
            fail(buf.toString());
        }
    }

    /**
     * Asserts that the string (<code>haystack</code>) starts with the string (
     * <code>expected</code>)
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param expected the expected starts with text
     */
    public static void assertStartsWith(String msg, String haystack, String expected)
    {
        assertNotNull(haystack, msg + ": haystack should not be null");
        assertNotNull(expected, msg + ": expected should not be null");

        if (!haystack.startsWith(expected))
        {
            StringBuffer buf = new StringBuffer();
            buf.append(msg).append(": String \"");
            int len = Math.min(expected.length() + 4, haystack.length());
            buf.append(haystack.substring(0, len));
            buf.append("\" does not start with expected \"").append(expected).append('\"');
            System.err.println(buf);
            fail(buf.toString());
        }
    }
}