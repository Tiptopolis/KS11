package com.uchump.prime.Metatron.Lib._Hamcrest.internal;

import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.SelfDescribing;

public class SelfDescribingValue<T> implements SelfDescribing {

    private T value;

    public SelfDescribingValue(T value) {
        this.value = value;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(value);
    }

}