package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;

import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.matchers.IsOrderedCollectionContaining;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.matchers.RegexMatcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matchers;

/**
 * Extra Matchers for the Junit Hamcrest users out there.
 */
public class ExtraMatchers
{
    /**
     * Creates a matcher for {@link Iterable}s that matches when consecutive passes over the
     * examined {@link Iterable} yield at least one item that is matched by the corresponding
     * matcher from the specified <code>itemMatchers</code>. Whilst matching, each traversal of
     * the examined {@link Iterable} will stop as soon as a matching item is found.
     * <p>
     * For example:
     *
     * <pre>
     * assertThat(Arrays.asList(&quot;foo&quot;,&quot;bar&quot;,&quot;baz&quot;),hasItems(endsWith(&quot;z&quot;),endsWith(&quot;o&quot;)))
     * </pre>
     *
     * @param itemMatchers the matchers to apply to items provided by the examined {@link Iterable}
     * @param <T> the type
     * @return the matcher
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Matcher<java.lang.Iterable<? super T>> ordered(List<T> itemMatchers)
    {
        return new IsOrderedCollectionContaining(itemMatchers);
    }

    /**
     * Create a matcher for {@link String} that matches against a regex pattern.
     *
     * <p>
     * Returns success based on {@code java.util.regex.Pattern.matcher(input).matches();}
     * </p>
     *
     * @param pattern the {@link java.util.regex.Pattern} syntax pattern to match against.
     * @return the Regex Matcher
     * @deprecated use {@link Matchers#matchesRegex(String)} instead
     */
    @Deprecated
    public static Matcher<java.lang.String> regex(String pattern)
    {
        return new RegexMatcher(pattern);
    }
}