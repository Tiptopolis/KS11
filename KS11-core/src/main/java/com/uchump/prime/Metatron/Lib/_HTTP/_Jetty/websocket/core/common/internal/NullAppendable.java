package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Utf8Appendable;

public class NullAppendable extends Utf8Appendable
{
    public NullAppendable()
    {
        super(new Appendable()
        {
            @Override
            public Appendable append(CharSequence csq)
            {
                return null;
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end)
            {
                return null;
            }

            @Override
            public Appendable append(char c)
            {
                return null;
            }
        });
    }

    @Override
    public int length()
    {
        return 0;
    }

    @Override
    public String getPartialString()
    {
        return null;
    }
}