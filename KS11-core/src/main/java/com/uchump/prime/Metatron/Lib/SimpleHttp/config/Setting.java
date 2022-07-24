package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
public interface Setting<T> {
    void applyTo(Configurable<T> configurable);
}