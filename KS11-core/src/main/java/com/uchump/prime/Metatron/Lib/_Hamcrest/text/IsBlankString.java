package com.uchump.prime.Metatron.Lib._Hamcrest.text;

import java.util.regex.Pattern;

import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeMatcher;


import static com.uchump.prime.Metatron.Lib._Hamcrest.core.AnyOf.anyOf;
import static com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNull.nullValue;

/**
 * Matches blank Strings (and null).
 */
public final class IsBlankString extends TypeSafeMatcher<String> {

    private static final IsBlankString BLANK_INSTANCE = new IsBlankString();
    @SuppressWarnings("unchecked")
    private static final Matcher<String> NULL_OR_BLANK_INSTANCE = anyOf(nullValue(), BLANK_INSTANCE);

    private static final Pattern REGEX_WHITESPACE = Pattern.compile("\\s*");

    private IsBlankString() { }

    @Override
    public boolean matchesSafely(String item) {
        return REGEX_WHITESPACE.matcher(item).matches();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a blank string");
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string contains
     * zero or more whitespace characters and nothing else.
     * For example:
     * <pre>assertThat("  ", is(blankString()))</pre>
     *
     * @return The matcher.
     */
    public static Matcher<String> blankString() {
        return BLANK_INSTANCE;
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is <code>null</code>, or
     * contains zero or more whitespace characters and nothing else.
     * For example:
     * <pre>assertThat(((String)null), is(blankOrNullString()))</pre>
     *
     * @return The matcher.
     */
    public static Matcher<String> blankOrNullString() {
        return NULL_OR_BLANK_INSTANCE;
    }

}