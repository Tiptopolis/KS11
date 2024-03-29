package com.uchump.prime.Metatron.Lib._Hamcrest.text;

import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeMatcher;

/**
 * Tests if a string is equal to another string, regardless of the case.
 */
public class IsEqualIgnoringCase extends TypeSafeMatcher<String> {

    // TODO: Replace String with CharSequence to allow for easy interoperability between
    //       String, StringBuffer, StringBuilder, CharBuffer, etc (joe).

    private final String string;

    public IsEqualIgnoringCase(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Non-null value required");
        }
        this.string = string;
    }

    @Override
    public boolean matchesSafely(String item) {
        return string.equalsIgnoreCase(item);
    }

    @Override
    public void describeMismatchSafely(String item, Description mismatchDescription) {
      mismatchDescription.appendText("was ").appendValue(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string equal to ")
                .appendValue(string)
                .appendText(" ignoring case");
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is equal to
     * the specified expectedString, ignoring case.
     * For example:
     * <pre>assertThat("Foo", equalToIgnoringCase("FOO"))</pre>
     *
     * @param expectedString
     *     the expected value of matched strings
     * @return The matcher.
     */
    public static Matcher<String> equalToIgnoringCase(String expectedString) {
        return new IsEqualIgnoringCase(expectedString);
    }

}