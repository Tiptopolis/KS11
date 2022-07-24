package com.uchump.prime.Metatron.Lib._HTTP._Spark;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Types and methods annotated with this annotation are experimental and may be changed or removed from the API at any
 * time. (This does not mean it won't be part of the official API at any point either).
 *
 * @author Per Wendel
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Experimental {
    String value() default "";
}