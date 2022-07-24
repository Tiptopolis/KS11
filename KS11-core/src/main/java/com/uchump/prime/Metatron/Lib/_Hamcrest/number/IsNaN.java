package com.uchump.prime.Metatron.Lib._Hamcrest.number;

import static com.uchump.prime.Metatron.Lib._Hamcrest.core.IsEqual.equalTo;

import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeMatcher;


/**
 * Is the value a number actually not a number (NaN)?
 */
public final class IsNaN extends TypeSafeMatcher<Double> {

    private IsNaN() { }

    @Override
    public boolean matchesSafely(Double item) {
        return Double.isNaN(item);
    }

    @Override
    public void describeMismatchSafely(Double item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendValue(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a double value of NaN");
    }

    /**
     * Creates a matcher of {@link Double}s that matches when an examined double is not a number.
     * For example:
     * <pre>assertThat(Double.NaN, is(notANumber()))</pre>
     *
     * @return The matcher.
     */
    public static Matcher<Double> notANumber() {
        return new IsNaN();
    }

}