package com.uchump.prime.Core.Utils.X;

import static com.uchump.prime.Core.Math.N_Operator.*;

@FunctionalInterface
public interface _OperatorX<T> {

	public T operate(T... t);

	public static enum Math implements _OperatorX<Number> {
		ADD(new _OperatorX<Number>() {
			@Override
			public Number operate(Number... n) {
				Number r = resolveTo(n[0], 0);
				for (int i = 0; i < n.length; i++)
					if (i != 0)
						r = add(r, n[i]);

				return r;
			}
		}), SUB(new _OperatorX<Number>() {
			@Override
			public Number operate(Number... n) {
				Number r = resolveTo(n[0], 0);
				for (int i = 0; i < n.length; i++)
					if (i != 0)
						r = sub(r, n[i]);

				return r;
			}
		}), MUL(new _OperatorX<Number>() {
			@Override
			public Number operate(Number... n) {
				Number r = resolveTo(n[0], 0);
				for (int i = 0; i < n.length; i++)
					if (i != 0)
						r = mul(r, n[i]);

				return r;
			}
		}), DIV(new _OperatorX<Number>() {
			@Override
			public Number operate(Number... n) {
				Number r = resolveTo(n[0], 0);
				for (int i = 0; i < n.length; i++)
					if (i != 0)
						r = div(r, n[i]);

				return r;
			}
		}),;

		public String Local_Name;
		public _OperatorX Fn;

		private Math(_OperatorX f) {
			this.Local_Name = this.name();
			this.Fn = f;
		}

		@Override
		public Number operate(Number... t) {

			return (Number) this.Fn.operate(t);
		}

	}
}
