package com.uchump.prime.Core.Utils.X;

import java.util.function.Function;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Utils.aMaths;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;

//operator
public interface _AlgebraX<N> {
	N operate(N a, N b);

	// binary operators
	public static enum Math implements _AlgebraX<Number> {
		ADD((a, b) -> {
			return N_Operator.add((Number) a, (Number) b);
		}), SUB((a, b) -> {
			return N_Operator.sub((Number) a, (Number) b);
		}), MUL((a, b) -> {
			return N_Operator.mul((Number) a, (Number) b);
		}), DIV((a, b) -> {
			return N_Operator.div((Number) a, (Number) b);
		}), MOD((a, b) -> {
			return N_Operator.mod((Number) a, (Number) b);
		}), POW((a, b) -> {
			return N_Operator.pow((Number) a, (Number) b);
		}), ROOT((a, b) -> {
			return N_Operator.root((Number) a, (Number) b);
		}), PERC((a, b) -> {
			return aMaths.inverseLerp(0, (Number) a, (Number) b);
		}), PERC_OF((a, b) -> {
			return aMaths.lerp(0, (Number) a, (Number) b);
		});

		// LOCAL_NAME
		// METHOD
		public String Local_Name;
		public aList<String> symbols;

		private _AlgebraX Fn;

		private Math(_AlgebraX f) {

			this.Local_Name = this.name();
			this.Fn = f;
		}

		@Override
		public Number operate(Number a, Number b) {
			return (Number) this.Fn.operate(a, b);
		}

		public static Number operate(Number a, Math op, Number b) {
			return op.operate(a, b);
		}

		public static Number operate(Math op, Number... N) {
			Number res = N_Operator.resolveTo(0, N[0]);

			for (Number n : N)
				res = op.operate(n, res);

			return res;
		}

	}

}