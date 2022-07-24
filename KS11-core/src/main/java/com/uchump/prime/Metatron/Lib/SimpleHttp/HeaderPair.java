package com.uchump.prime.Metatron.Lib.SimpleHttp;
import java.util.Objects;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Header;

import static java.lang.String.format;

public final class HeaderPair implements Header {

    private final String name;
    private final String value;

    private HeaderPair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Header header(String name, String value) {
        return new HeaderPair(name, value);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderPair that = (HeaderPair) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return format("%s{name='%s', value='%s'}", this.getClass().getSimpleName(), name, value);
    }
}