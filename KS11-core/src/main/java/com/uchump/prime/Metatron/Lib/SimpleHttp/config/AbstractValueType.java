package com.uchump.prime.Metatron.Lib.SimpleHttp.config;
import static java.lang.String.format;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.ValueType;

public class AbstractValueType<T> implements ValueType<T> {

    protected final T value;

    public AbstractValueType(T value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AbstractValueType))
            return false;
        AbstractValueType that = (AbstractValueType) o;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public final int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return format("%s[%s]", this.getClass().getSimpleName(), value);
    }
}