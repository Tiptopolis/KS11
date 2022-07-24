package com.uchump.prime.Metatron.Lib._HTTP._Spark;
class Base64 {

    //CS304 Issue link:https://github.com/perwendel/spark/issues/1061

    private static final java.util.Base64.Encoder urlEncoder = java.util.Base64.getUrlEncoder();
    private static final java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();

    /**
     * @param toEncodeContent the String to be encode
     * @return String after encode
     */
    public static String encode(String toEncodeContent) {
        if (toEncodeContent == null) {
            return null;
        }
        return urlEncoder.encodeToString(toEncodeContent.getBytes());
    }

    //CS304 Issue link:https://github.com/perwendel/spark/issues/1061

    /**
     * @param toDecodeContent the String to be decode
     * @return String after decode
     */
    public static String decode(String toDecodeContent) {
        if (toDecodeContent == null) {
            return null;
        }
        byte[] buf = null;
        try {
            buf = decoder.decode(toDecodeContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(buf);
    }


}