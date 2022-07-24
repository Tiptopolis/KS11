package com.uchump.prime.Metatron.Lib._HTTP._Jetty.http;
import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Index;
/**
 * HTTP and WebSocket Schemes
 */
public enum HttpScheme
{
    HTTP("http", 80),
    HTTPS("https", 443),
    WS("ws", 80),
    WSS("wss", 443);

    public static final Index<HttpScheme> CACHE = new Index.Builder<HttpScheme>()
        .caseSensitive(false)
        .withAll(HttpScheme.values(), HttpScheme::asString)
        .build();

    private final String _string;
    private final ByteBuffer _buffer;
    private final int _defaultPort;

    HttpScheme(String s, int port)
    {
        _string = s;
        _buffer = BufferUtil.toBuffer(s);
        _defaultPort = port;
    }

    public ByteBuffer asByteBuffer()
    {
        return _buffer.asReadOnlyBuffer();
    }

    public boolean is(String s)
    {
        return _string.equalsIgnoreCase(s);
    }

    public String asString()
    {
        return _string;
    }

    public int getDefaultPort()
    {
        return _defaultPort;
    }

    public int normalizePort(int port)
    {
        return port == _defaultPort ? 0 : port;
    }

    @Override
    public String toString()
    {
        return _string;
    }

    public static int getDefaultPort(String scheme)
    {
        HttpScheme httpScheme = scheme == null ? null : CACHE.get(scheme);
        return httpScheme == null ? HTTP.getDefaultPort() : httpScheme.getDefaultPort();
    }

    public static int normalizePort(String scheme, int port)
    {
        HttpScheme httpScheme = scheme == null ? null : CACHE.get(scheme);
        return httpScheme == null ? port : httpScheme.normalizePort(port);
    }
}