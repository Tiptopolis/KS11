package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ResourceCollators
{
    private static Comparator<? super Resource> BY_NAME_ASCENDING =
        new Comparator<>()
        {
            private final Collator collator = Collator.getInstance(Locale.ENGLISH);

            @Override
            public int compare(Resource o1, Resource o2)
            {
                return collator.compare(o1.getName(), o2.getName());
            }
        };

    private static Comparator<? super Resource> BY_NAME_DESCENDING =
        Collections.reverseOrder(BY_NAME_ASCENDING);

    private static Comparator<? super Resource> BY_LAST_MODIFIED_ASCENDING =
        Comparator.comparingLong(Resource::lastModified);

    private static Comparator<? super Resource> BY_LAST_MODIFIED_DESCENDING =
        Collections.reverseOrder(BY_LAST_MODIFIED_ASCENDING);

    private static Comparator<? super Resource> BY_SIZE_ASCENDING =
        Comparator.comparingLong(Resource::length);

    private static Comparator<? super Resource> BY_SIZE_DESCENDING =
        Collections.reverseOrder(BY_SIZE_ASCENDING);

    public static Comparator<? super Resource> byLastModified(boolean sortOrderAscending)
    {
        if (sortOrderAscending)
        {
            return BY_LAST_MODIFIED_ASCENDING;
        }
        else
        {
            return BY_LAST_MODIFIED_DESCENDING;
        }
    }

    public static Comparator<? super Resource> byName(boolean sortOrderAscending)
    {
        if (sortOrderAscending)
        {
            return BY_NAME_ASCENDING;
        }
        else
        {
            return BY_NAME_DESCENDING;
        }
    }

    public static Comparator<? super Resource> bySize(boolean sortOrderAscending)
    {
        if (sortOrderAscending)
        {
            return BY_SIZE_ASCENDING;
        }
        else
        {
            return BY_SIZE_DESCENDING;
        }
    }
}