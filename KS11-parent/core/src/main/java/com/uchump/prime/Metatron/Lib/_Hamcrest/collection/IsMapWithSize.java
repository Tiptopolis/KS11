package com.uchump.prime.Metatron.Lib._Hamcrest.collection;


import java.util.Map;

import com.uchump.prime.Metatron.Lib._Hamcrest.FeatureMatcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import static com.uchump.prime.Metatron.Lib._Hamcrest.core.IsEqual.equalTo;


/**
 * Matches if map size satisfies a nested matcher.
 */
public final class IsMapWithSize<K, V> extends FeatureMatcher<Map<? extends K, ? extends V>, Integer> {

    @SuppressWarnings("WeakerAccess")
    public IsMapWithSize(Matcher<? super Integer> sizeMatcher) {
      super(sizeMatcher, "a map with size", "map size");
    }

    @Override
    protected Integer featureValueOf(Map<? extends K, ? extends V> actual) {
      return actual.size();
    }

    /**
     * Creates a matcher for {@link java.util.Map}s that matches when the <code>size()</code> method returns
     * a value that satisfies the specified matcher.
     * For example:
     * <pre>assertThat(myMap, is(aMapWithSize(equalTo(2))))</pre>
     *
     * @param <K>
     *     the map key type.
     * @param <V>
     *     the map value type.
     * @param sizeMatcher
     *     a matcher for the size of an examined {@link java.util.Map}
     * @return The matcher.
     */
    public static <K, V> Matcher<Map<? extends K, ? extends V>> aMapWithSize(Matcher<? super Integer> sizeMatcher) {
        return new IsMapWithSize<>(sizeMatcher);
    }

    /**
     * Creates a matcher for {@link java.util.Map}s that matches when the <code>size()</code> method returns
     * a value equal to the specified <code>size</code>.
     * For example:
     * <pre>assertThat(myMap, is(aMapWithSize(2)))</pre>
     *
     * @param <K>
     *     the map key type.
     * @param <V>
     *     the map value type.
     * @param size
     *     the expected size of an examined {@link java.util.Map}
     * @return The matcher.
     */
    public static <K, V> Matcher<Map<? extends K, ? extends V>> aMapWithSize(int size) {
        return IsMapWithSize.aMapWithSize(equalTo(size));
    }

    /**
     * Creates a matcher for {@link java.util.Map}s that matches when the <code>size()</code> method returns
     * zero.
     * For example:
     * <pre>assertThat(myMap, is(anEmptyMap()))</pre>
     *
     * @param <K>
     *     the map key type.
     * @param <V>
     *     the map value type.
     * @return The matcher.
     */
    public static <K, V> Matcher<Map<? extends K, ? extends V>> anEmptyMap() {
        return IsMapWithSize.aMapWithSize(equalTo(0));
    }

}