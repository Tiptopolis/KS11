package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <code>&#064;ManagedOperation</code> annotation is used to indicate that a given method
 * should be considered a JMX operation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
public @interface ManagedOperation
{
    /**
     * Description of the Managed Object
     *
     * @return value
     */
    String value() default "Not Specified";

    /**
     * The impact of an operation.
     *
     * NOTE: Valid values are UNKNOWN, ACTION, INFO, ACTION_INFO
     *
     * NOTE: applies to METHOD
     *
     * @return String representing the impact of the operation
     */
    String impact() default "UNKNOWN";

    /**
     * Does the managed field exist on a proxy object?
     *
     * @return true if a proxy object is involved
     */
    boolean proxied() default false;
}