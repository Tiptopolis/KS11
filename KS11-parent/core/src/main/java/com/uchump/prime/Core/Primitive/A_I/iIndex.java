package com.uchump.prime.Core.Primitive.A_I;

import static com.uchump.prime.Core.uAppUtils.*;

import com.uchump.prime.Core.Math.Utils.aMaths;

public interface iIndex<T> extends iGroup<Integer, T> {

	// __________i_s_______________________
	// InnerPos @2#10->2 @%#
	// OutterPos @12#10->2 @%#
	// InnerNeg @-2#10->8 abs(#+@)%#
	// OutterNeg @-12#10->8 abs(#+@)%#
	// need to make some Vector comparators & sorters

	@Override
	public default Integer firstIndex() {
		return 0;
	}

	// this @Override is purely for demonstration & convenience
	// it still indexes properly without it
	@Override
	public default Integer resolveIndex(Number n) {
		if (n.intValue() == 0)
			return 0;

		int s = this.size();
		int i = n.intValue();

		if (i < 0) {
			if (i + s > 0)
				i = Math.abs(s + i);
			else
				i = s - Math.abs(s + i);
		}

		if (i > s) {
			i = (i % s);
		}
		return i;
	}
}
