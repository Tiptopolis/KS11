package com.uchump.prime.Core.Primitive.A_I.L;

import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;

@FunctionalInterface
public interface iConstructor<T> {
	//_FN params
	public T getNew(_Map.Entry<String, Object>... args);

}
