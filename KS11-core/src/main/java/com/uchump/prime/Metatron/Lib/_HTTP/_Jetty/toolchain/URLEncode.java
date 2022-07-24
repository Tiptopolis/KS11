package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLEncode
{
    public static String encode(String value, String charset) throws UnsupportedEncodingException
    {
        return URLEncoder.encode(value, charset).replaceAll("\\*", "%2A");
    }
}