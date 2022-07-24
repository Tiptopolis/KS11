package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver;

/**
 * Used to indicate that a feature is not supported for the specific embedded server.
 */
public class NotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Raises a NotSupportedException for the provided class name and feature name.
     *
     * @param clazz   the class name
     * @param feature the feature name
     */
    public static void raise(String clazz, String feature) {
        throw new NotSupportedException(clazz, feature);
    }

    private NotSupportedException(String clazz, String feature) {
        super("'" + clazz + "' doesn't support '" + feature + "'");

    }

}