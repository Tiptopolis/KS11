package com.uchump.prime.Metatron.Lib._Hamcrest;

/**
 * TODO(ngd): Document.
 *
 * @param <T> the type of matcher being diagnosed.
 */
public abstract class DiagnosingMatcher<T> extends BaseMatcher<T> {

    @Override
    public final boolean matches(Object item) {
        return matches(item, Description.NONE);
    }

    @Override
    public final void describeMismatch(Object item, Description mismatchDescription) {
        matches(item, mismatchDescription);
    }

    protected abstract boolean matches(Object item, Description mismatchDescription);

}