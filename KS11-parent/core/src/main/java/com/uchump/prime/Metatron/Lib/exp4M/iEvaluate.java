package com.uchump.prime.Metatron.Lib.exp4M;

import com.uchump.prime.Core.Primitive.iFunctor;

public interface iEvaluate<N> extends iFunctor.Supplier<N> {
	public default N evaluate() {
		return this.get();
	}
}
