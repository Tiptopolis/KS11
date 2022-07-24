package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;
/**
 * Interface to decorate objects created by the {@link DecoratedObjectFactory}
 */
public interface Decorator
{
    <T> T decorate(T o);

    void destroy(Object o);
}