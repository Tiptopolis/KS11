package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <code>&#064;ManagedAttribute</code> annotation is used to indicate that a given method
 * exposes a JMX attribute. This annotation is placed always on the reader
 * method of a given attribute. Unless it is marked as read-only in the
 * configuration of the annotation a corresponding setter is looked for
 * following normal naming conventions. For example if this annotation is
 * on a method called getFoo() then a method called setFoo() would be looked
 * for and if found wired automatically into the jmx attribute.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
public @interface ManagedAttribute
{
    /**
     * Description of the Managed Attribute
     *
     * @return value
     */
    String value() default "Not Specified";

    /**
     * name to use for the attribute
     *
     * @return the name of the attribute
     */
    String name() default "";

    /**
     * Is the managed field read-only?
     *
     * Required only when a setter exists but should not be exposed via JMX
     *
     * @return true if readonly
     */
    boolean readonly() default false;

    /**
     * Does the managed field exist on a proxy object?
     *
     * @return true if a proxy object is involved
     */
    boolean proxied() default false;

    /**
     * If is a field references a setter that doesn't conform to standards for discovery
     * it can be set here.
     *
     * @return the full name of the setter in question
     */
    String setter() default "";
}