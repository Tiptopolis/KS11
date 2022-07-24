package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;

import java.util.HashSet;

public class AsciiLowerCaseSet extends HashSet<String>
{
    @Override
    public boolean add(String s)
    {
        return super.add(s == null ? null : StringUtil.asciiToLowerCase(s));
    }

    @Override
    public boolean contains(Object o)
    {
        if (o instanceof String)
            return super.contains(StringUtil.asciiToLowerCase((String)o));
        return super.contains(o);
    }
}