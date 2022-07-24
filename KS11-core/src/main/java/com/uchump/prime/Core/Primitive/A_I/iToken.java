package com.uchump.prime.Core.Primitive.A_I;

import java.io.Serializable;

public interface iToken<T> extends Serializable{
	public T get();
	public default String type()
	{
		return this.get().getClass().getSimpleName();
	}
}
