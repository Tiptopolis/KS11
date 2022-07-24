package com.uchump.prime.Metatron.Lib.SimpleHttp.A_I;

import com.uchump.prime.Core.Primitive.Com.iHeader;

public interface Headers extends Iterable<Header> {

    boolean has(String header);
    Header get(String header);

}