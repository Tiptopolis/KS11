package com.uchump.prime.Metatron.Lib.SimpleHttp;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class EmptyIterator<T> implements Iterator<T>, Serializable {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}