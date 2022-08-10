package com.uchump.prime.Core.Primitive.A_I.L;

@FunctionalInterface
public interface iMorpher<I, O> {
	public O as(I i);
}
