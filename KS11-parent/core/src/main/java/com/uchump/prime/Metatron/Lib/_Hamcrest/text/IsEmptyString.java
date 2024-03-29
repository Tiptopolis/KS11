package com.uchump.prime.Metatron.Lib._Hamcrest.text;

import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeMatcher;

import static com.uchump.prime.Metatron.Lib._Hamcrest.core.AnyOf.anyOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNull.nullValue;
/**
 * Matches empty Strings (and null).
 */
public final class IsEmptyString extends TypeSafeMatcher<String> {

    private static final IsEmptyString INSTANCE = new IsEmptyString();
    @SuppressWarnings("unchecked")
    private static final Matcher<String> NULL_OR_EMPTY_INSTANCE = anyOf(nullValue(), INSTANCE);

    private IsEmptyString() { }

    @Override
    public boolean matchesSafely(String item) {
        return item.equals("");
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an empty string");
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string has zero length.
     * For example:
     * <pre>assertThat("", isEmptyString())</pre>
     *
     * @deprecated use is(emptyString()) instead
     * @return The matcher.
     */
    @Deprecated
    public static Matcher<String> isEmptyString() {
        return emptyString();
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string has zero length.
     * For example:
     * <pre>assertThat("", is(emptyString()))</pre>
     *
     * @return The matcher.
     */
    public static Matcher<String> emptyString() {
        return INSTANCE;
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is <code>null</code>, or
     * has zero length.
     * For example:
     * <pre>assertThat(((String)null), isEmptyOrNullString())</pre>
     *
     * @deprecated use is(emptyOrNullString()) instead
     * @return The matcher.
     */
    @Deprecated
    public static Matcher<String> isEmptyOrNullString() {
        return emptyOrNullString();
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is <code>null</code>, or
     * has zero length.
     * For example:
     * <pre>assertThat(((String)null), is(emptyOrNullString()))</pre>
     *
     * @return The matcher.
     */
    public static Matcher<String> emptyOrNullString() {
        return NULL_OR_EMPTY_INSTANCE;
    }

}