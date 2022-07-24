package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
public class Password extends AbstractValueType<String> {

    public static Password password(String value) {
        return new Password(value);
    }

    private Password(String value) {
        super(value);
    }
}