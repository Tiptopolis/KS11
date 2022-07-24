package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
public class AutomaticRedirectHandling extends AbstractValueType<Boolean> implements Setting<Boolean> {

    public static AutomaticRedirectHandling on() {
        return new AutomaticRedirectHandling(true);
    }

    public static AutomaticRedirectHandling off() {
        return new AutomaticRedirectHandling(false);
    }

    private AutomaticRedirectHandling(Boolean automaticallyHandleRedirects) {
        super(automaticallyHandleRedirects);
    }

    @Override
    public void applyTo(Configurable<Boolean> configurable) {
        configurable.setTo(value);
    }
}