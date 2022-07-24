package com.uchump.prime.Metatron.Lib._Hamcrest.collection;

import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeMatcher;

/**
 * Tests if collection is empty.
 */
public class IsEmptyIterable<E> extends TypeSafeMatcher<Iterable<? extends E>> {

    @Override
    public boolean matchesSafely(Iterable<? extends E> iterable) {
        return !iterable.iterator().hasNext();
    }
    @Override
    public void describeMismatchSafely(Iterable<? extends E> iter, Description mismatchDescription) {
        mismatchDescription.appendValueList("[", ",", "]", iter);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an empty iterable");
    }

    /**
     * Creates a matcher for {@link Iterable}s matching examined iterables that yield no items.
     * For example:
     * <pre>assertThat(new ArrayList&lt;String&gt;(), is(emptyIterable()))</pre>
     *
     * @param <E>
     *     the matcher type.
     * @return The matcher.
     */
    public static <E> Matcher<Iterable<? extends E>> emptyIterable() {
        return new IsEmptyIterable<>();
    }

    /**
     * Creates a matcher for {@link Iterable}s matching examined iterables that yield no items.
     * For example:
     * <pre>assertThat(new ArrayList&lt;String&gt;(), is(emptyIterableOf(String.class)))</pre>
     *
     * @param <E>
     *     the matcher type.
     * @param unusedToForceReturnType
     *     the type of the iterable's content
     * @return The matcher.
     */
    @SuppressWarnings({"unchecked", "UnusedParameters"})
    public static <E> Matcher<Iterable<E>> emptyIterableOf(Class<E> unusedToForceReturnType) {
      return (Matcher)emptyIterable();
    }

}