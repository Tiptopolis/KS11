package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
import java.net.URL;

public class Proxy extends AbstractValueType<URL> implements Setting<URL> {

    public static Proxy proxy(URL value) {
        return new Proxy(value);
    }

    protected Proxy(URL value) {
        super(value);
    }

    @Override
    public void applyTo(Configurable<URL> configurable) {
        configurable.setTo(value);
    }

}