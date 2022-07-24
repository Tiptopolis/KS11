package com.uchump.prime.Core.System.Environment;

import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.aVectorUtils;
import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.A_I.iSpace;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;

public class _VectorSpace<T> extends aValue<aMultiMap<aVector, T>> {

	public aVector offset;
	public aVector orientation;
	public aVector unit;
	
	public _VectorSpace(T defaultVal, aVector size) {
		super("Space", new aMultiMap<aVector, T>());
		aVector[] V = aVectorUtils.fillPermutations(size);
		for (int i = 0; i < V.length; i++)
			this.value.put(V[i], defaultVal);
	}

}
