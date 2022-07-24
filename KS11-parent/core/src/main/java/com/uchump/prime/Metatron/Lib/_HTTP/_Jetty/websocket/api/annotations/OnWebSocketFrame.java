package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * (ADVANCED) Annotation for tagging methods to receive frame events.
 * <p>
 * Acceptable method patterns.<br>
 * Note: {@code methodName} can be any name you want to use.
 * <ol>
 * <li><code>public void methodName({@link Frame} frame)</code></li>
 * <li><code>public void methodName({@link Session} session, {@link Frame} frame)</code></li>
 * </ol>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
    {ElementType.METHOD})
public @interface OnWebSocketFrame
{
    /* no config */
}