package com.uchump.prime.Metatron.Lib._Hamcrest.core;

import com.uchump.prime.Metatron.Lib._Hamcrest.BaseMatcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;

/**
 * Is the value the same object as another value?
 */
public class IsSame<T> extends BaseMatcher<T> {

    private final T object;

    public IsSame(T object) {
        this.object = object;
    }

    @Override
    public boolean matches(Object arg) {
        return arg == object;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("sameInstance(")
                .appendValue(object)
                .appendText(")");
    }

    /**
     * Creates a matcher that matches only when the examined object is the same instance as
     * the specified target object.
     *
     * @param <T>
     *     the matcher type.
     * @param target
     *     the target instance against which others should be assessed
     * @return The matcher.
     */
    public static <T> Matcher<T> sameInstance(T target) {
        return new IsSame<>(target);
    }

    /**
     * Creates a matcher that matches only when the examined object is the same instance as
     * the specified target object.
     *
     * @param <T>
     *     the matcher type.
     * @param target
     *     the target instance against which others should be assessed
     * @return The matcher.
     */
    public static <T> Matcher<T> theInstance(T target) {
        return new IsSame<>(target);
    }

}