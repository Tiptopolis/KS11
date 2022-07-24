package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
public class AccessToken extends AbstractValueType<String> {

    public static AccessToken accessToken(String value) {
        return new AccessToken(value);
    }

    private AccessToken(String value) {
        super(value);
    }
}