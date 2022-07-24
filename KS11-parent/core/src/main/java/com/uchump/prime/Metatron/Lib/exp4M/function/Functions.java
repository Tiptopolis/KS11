package com.uchump.prime.Metatron.Lib.exp4M.function;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Utils.StringUtils;

public class Functions {

	public static aList<aFunction> BUILT_IN = new aList<aFunction>();
	static {
		BUILT_IN.append(new aFunction<Number>("sin") {
		

			@Override
			public Number apply(Number t) {

				return N_Operator.sin(t);
			}

		});

		BUILT_IN.append(new aFunction<Number>("cos") {

		

			@Override
			public Number apply(Number t) {

				return N_Operator.cos(t);
			}

		});

		BUILT_IN.append(new aFunction<Number>("tan") {

			

			@Override
			public Number apply(Number t) {

				return N_Operator.tan(t);
			}

		});
	}

	public static aFunction getA(String name) {
		for (aFunction F : BUILT_IN)
			if (F.equals(name))
				return F;

		return null;
	}
}
