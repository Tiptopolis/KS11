package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.matchers;

import java.util.regex.Pattern;

import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matchers;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeMatcher;

/**
 * @deprecated use {@link Matchers#matchesRegex(String)} instead
 */
@Deprecated
public class RegexMatcher extends TypeSafeMatcher
{
    private final Pattern pattern;

    public RegexMatcher(String pattern)
    {
        this(Pattern.compile(pattern));
    }

    public RegexMatcher(Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText("matches regular expression ").appendValue(pattern);
    }

    @Override
    protected boolean matchesSafely(Object item)
    {
        if (item == null)
            return false;
        return pattern.matcher(item.toString()).matches();
    }
}