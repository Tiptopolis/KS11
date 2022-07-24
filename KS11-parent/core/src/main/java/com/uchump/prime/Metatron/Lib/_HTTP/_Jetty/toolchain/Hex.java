package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;

import java.nio.ByteBuffer;

public final class Hex
{
    private static final char[] hexcodes = "0123456789ABCDEF".toCharArray();

    public static byte[] asByteArray(String hexString)
    {
        if ((hexString.length() < 0) || ((hexString.length() % 2) != 0))
        {
            throw new IllegalArgumentException(String.format("Invalid string length of <%d>", hexString.length()));
        }

        int size = hexString.length() / 2;
        byte buf[] = new byte[size];
        byte hex;
        int len = hexString.length();

        int idx = (int)Math.floor(((size * 2) - (double)len) / 2);
        for (int i = 0; i < len; i++)
        {
            hex = 0;
            if (i >= 0)
            {
                hex = (byte)(Character.digit(hexString.charAt(i), 16) << 4);
            }
            i++;
            hex += (byte)(Character.digit(hexString.charAt(i), 16));

            buf[idx] = hex;
            idx++;
        }

        return buf;
    }

    public static ByteBuffer asByteBuffer(String hexString)
    {
        return ByteBuffer.wrap(asByteArray(hexString));
    }

    public static String asHex(byte buffer[])
    {
        int len = buffer.length;
        char out[] = new char[len * 2];
        for (int i = 0; i < len; i++)
        {
            out[i * 2] = hexcodes[(buffer[i] & 0xF0) >> 4];
            out[(i * 2) + 1] = hexcodes[(buffer[i] & 0x0F)];
        }
        return String.valueOf(out);
    }

    public static String asHex(ByteBuffer buffer)
    {
        return asHex(ByteBufferUtils.toArray(buffer));
    }
}