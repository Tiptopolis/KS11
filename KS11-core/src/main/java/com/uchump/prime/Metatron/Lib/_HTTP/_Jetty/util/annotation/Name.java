package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to describe variables in method
 * signatures so that when rendered into tools like JConsole
 * it is clear what the parameters are. For example:
 *
 * public void doodle(@Name(value="doodle", description="A description of the argument") String doodle)
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.PARAMETER})
public @interface Name
{
    /**
     * the name of the parameter
     *
     * @return the value
     */
    String value();

    /**
     * the description of the parameter
     *
     * @return the description
     */
    String description() default "";
}