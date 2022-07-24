package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;
/**
 * ClassVisibilityChecker
 *
 * Interface to be implemented by classes capable of checking class visibility
 * for a context.
 */
public interface ClassVisibilityChecker
{

    /**
     * Is the class a System Class.
     * A System class is a class that is visible to a webapplication,
     * but that cannot be overridden by the contents of WEB-INF/lib or
     * WEB-INF/classes
     *
     * @param clazz The fully qualified name of the class.
     * @return True if the class is a system class.
     */
    boolean isSystemClass(Class<?> clazz);

    /**
     * Is the class a Server Class.
     * A Server class is a class that is part of the implementation of
     * the server and is NIT visible to a webapplication. The web
     * application may provide it's own implementation of the class,
     * to be loaded from WEB-INF/lib or WEB-INF/classes
     *
     * @param clazz The fully qualified name of the class.
     * @return True if the class is a server class.
     */
    boolean isServerClass(Class<?> clazz);
}