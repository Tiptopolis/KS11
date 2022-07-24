package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
public class Username extends AbstractValueType<String> {

    public static Username username(String value) {
        return new Username(value);
    }

    private Username(String value) {
        super(value);
    }
}