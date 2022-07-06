package com.uchump.prime.Core.Primitive.A_I;

@FunctionalInterface
public interface iMorpher<I, O> {
	public O as(I i);
}
