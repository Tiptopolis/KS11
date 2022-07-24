package com.uchump.prime.Metatron.Lib.SimpleHttp.A_I;

import java.util.concurrent.Callable;

public interface Executor<E extends Exception> {
    <V> V submit(Callable<V> callable) throws E;
}