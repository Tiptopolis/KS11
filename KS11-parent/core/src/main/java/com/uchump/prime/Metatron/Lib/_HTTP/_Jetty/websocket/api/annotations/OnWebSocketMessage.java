package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations;


import java.io.Reader;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for tagging methods to receive Binary or Text Message events.
 * <p>
 * Acceptable method patterns.<br>
 * Note: {@code methodName} can be any name you want to use.
 * <p>
 * <u>Text Message Versions</u>
 * <ol>
 * <li>{@code public void methodName(String text)}</li>
 * <li>{@code public void methodName(Session session, String text)}</li>
 * <li>{@code public void methodName(Reader reader)}</li>
 * <li>{@code public void methodName(Session session, Reader reader)}</li>
 * </ol>
 * <p>Note: that the {@link Reader} in this case will always use UTF-8 encoding/charset (this is dictated by the RFC 6455 spec for Text Messages. If you need to
 * use a non-UTF-8 encoding/charset, you are instructed to use the binary messaging techniques.</p>
 * <u>Binary Message Versions</u>
 * <ol>
 * <li>{@code public void methodName(ByteBuffer message)}</li>
 * <li>{@code public void methodName(Session session, ByteBuffer message)}</li>
 * <li>{@code public void methodName(byte[] buf, int offset, int length)}</li>
 * <li>{@code public void methodName(Session session, byte[] buf, int offset, int length)}</li>
 * <li>{@code public void methodName(InputStream stream)}</li>
 * <li>{@code public void methodName(Session session, InputStream stream)}</li>
 * </ol>
 * <u>Partial Message Variations</u>
 * <p>These are used to receive partial messages without aggregating them into a complete WebSocket message. Instead the a boolean
 * argument is supplied to indicate whether this is the last segment of data of the message. See {@link WebSocketPartialListener}
 * interface for more details on partial messages.</p>
 * <ol>
 * <li>{@code public void methodName(ByteBuffer payload, boolean last)}</li>
 * <li>{@code public void methodName(String payload, boolean last)}</li>
 * </ol>
 * <p>Note: Similar to the signatures above these can all be used with an optional first {@link Session} parameter.</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
    {ElementType.METHOD})
public @interface OnWebSocketMessage
{
    /* no config */
}