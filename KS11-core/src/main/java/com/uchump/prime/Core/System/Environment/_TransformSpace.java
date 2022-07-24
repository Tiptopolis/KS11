package com.uchump.prime.Core.System.Environment;

import java.util.function.Supplier;

import com.uchump.prime.Core.Math.Primitive.aTransform;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.aVectorUtils;
import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;

public class _TransformSpace<T> extends aValue<aMultiMap<aTransform, T>> {
//volumetric collision space, dynamic relative info

	public aTransform basis; // offset, orientation, unit
	public Supplier<aVector> offset;
	public Supplier<aVector> orientation;
	public Supplier<aVector> unit;

	public _TransformSpace(T defaultVal, aVector size) {
		super("Space", new aMultiMap<aTransform, T>());
		this.basis = aTransform.SpatialBasis;
		aVector[] V = aVectorUtils.fillPermutations(size);
		for (int i = 0; i < V.length; i++)
			this.value.put(new aTransform(V[i]), defaultVal);

		this.offset = () -> (this.basis.getPosition());
		this.orientation = () -> this.basis.getRotation();
		this.unit = () -> this.basis.getScale();
	}

}
