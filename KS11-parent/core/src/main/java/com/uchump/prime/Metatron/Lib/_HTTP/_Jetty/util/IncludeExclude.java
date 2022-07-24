package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Utility class to maintain a set of inclusions and exclusions.
 * <p>This extension of the {@link IncludeExcludeSet} class is used
 * when the type of the set elements is the same as the type of
 * the predicate test.
 * <p>
 *
 * @param <ITEM> The type of element
 */
public class IncludeExclude<ITEM> extends IncludeExcludeSet<ITEM, ITEM>
{
    public IncludeExclude()
    {
        super();
    }

    public <SET extends Set<ITEM>> IncludeExclude(Class<SET> setClass)
    {
        super(setClass);
    }

    public <SET extends Set<ITEM>> IncludeExclude(Set<ITEM> includeSet, Predicate<ITEM> includePredicate, Set<ITEM> excludeSet,
                                                  Predicate<ITEM> excludePredicate)
    {
        super(includeSet, includePredicate, excludeSet, excludePredicate);
    }
}