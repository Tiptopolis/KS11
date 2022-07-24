package com.uchump.prime.Metatron.Lib.SimpleHttp;
import java.util.Iterator;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Header;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;

public final class EmptyHeaders implements Headers {

    public static EmptyHeaders emptyHeaders() {
        return new EmptyHeaders();
    }

    private EmptyHeaders() {
    }

    @Override
    public Iterator<Header> iterator() {
        return new EmptyIterator<>();
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean has(String header) {
        return false;
    }

    @Override
    public Header get(String header) {
        return new NoHeader();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        return other != null && getClass() == other.getClass();
    }
}