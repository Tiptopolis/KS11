package com.uchump.prime.Metatron.Lib._Hamcrest.internal;
import java.util.ArrayList;
import java.util.List;

import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNull;

public class NullSafety {

    @SuppressWarnings("unchecked")
    public static <E> List<Matcher<? super E>> nullSafe(Matcher<? super E>[] itemMatchers) {
        final List<Matcher<? super E>> matchers = new ArrayList<>(itemMatchers.length);
        for (final Matcher<? super E> itemMatcher : itemMatchers) {
            matchers.add((Matcher<? super E>) (itemMatcher == null ? IsNull.nullValue() : itemMatcher));
        }
        return matchers;
    }

}