package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
import java.time.Duration;

public class HttpTimeout extends AbstractValueType<Duration> implements Setting<Integer> {

    public static HttpTimeout httpTimeout(Duration value) {
        return new HttpTimeout(value);
    }

    private HttpTimeout(Duration value) {
        super(value);
    }

    @Override
    public void applyTo(Configurable<Integer> configurable) {
        configurable.setTo((int) value.toMillis());
    }
}