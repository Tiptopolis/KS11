package com.uchump.prime.Core.Math.Primitive;

import java.util.Comparator;

import com.badlogic.gdx.math.MathUtils;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Utils.aMaths;

public class uVector {

	// annotated vector

	// map a value to a range/ratio/gradient
	// avg axial space dst
	public Comparator domainComparator() {
		// compare by length of vector
		return new Comparator<aVector>() {

			@Override
			public int compare(aVector o1, aVector o2) {

				if (o1.size() > o2.size())
					return 1;
				if (o1.size() == o2.size())
					return 0;
				if (o1.size() < o2.size())
					return -1;

				return 0;
			}
		};
	}

	public static Comparator sumComparator() {
		return new Comparator<aVector>() {
			@Override
			public int compare(aVector o1, aVector o2) {

				float O1 = o1.sum().floatValue();
				float O2 = o1.sum().floatValue();
				if (O1 > O2)
					return 1;
				if (N_Operator.isEqual(O1, O2))
					return 0;
				if (O1 < O2)
					return -1;

				return 0;
			}
		};
	}

	public static Comparator distanceComparator(aVector point, Class type) {
		final aVector finalP = point.cpy();
		if (type.equals(aVector.class)) {
			return new Comparator<aVector>() {
				@Override
				public int compare(aVector p0, aVector p1) {
					if (p0 == null || p1 == null)
						return 0;

					double ds0 = p0.dstLen(finalP);
					double ds1 = p1.dstLen(finalP);

					if (aMaths.isEqual(ds0, ds1, MathUtils.FLOAT_ROUNDING_ERROR * 2f)) {
						ds1 += (MathUtils.FLOAT_ROUNDING_ERROR * 8f);
						ds0 -= (MathUtils.FLOAT_ROUNDING_ERROR);
					}

					return Double.compare(ds1, ds0);
				}

			};
		}

		return null;
	}

}
