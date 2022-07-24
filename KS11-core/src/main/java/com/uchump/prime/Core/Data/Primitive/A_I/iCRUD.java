package com.uchump.prime.Core.Data.Primitive.A_I;

import java.util.function.Function;

import com.uchump.prime.Core.Primitive.iFunctor;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;

public interface iCRUD<I, T> {

	// aNumber implements CharSequence ergo aVector does, too

	public T create(aMap<String, T> params);

	public <X> T create(Function<X, T> f);

	public T create(iFunctor.Effect<T> f);

	public T create(Object entry);

	public T create(Object... entries);

	public T read(I indx);

	public T[] read(I from, I to);

	public void update(I index, T entry);

	public void update(I index, iFunctor.Effect<T> f);

	public void delete(I index);

	public void delete(I from, I to);

}
