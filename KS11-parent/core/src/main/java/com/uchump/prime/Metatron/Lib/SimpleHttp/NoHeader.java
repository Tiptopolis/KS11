package com.uchump.prime.Metatron.Lib.SimpleHttp;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Header;

public final class NoHeader implements Header {
    @Override
    public String name() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String value() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) 
            return false;
        return that.getClass().equals(this.getClass());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}