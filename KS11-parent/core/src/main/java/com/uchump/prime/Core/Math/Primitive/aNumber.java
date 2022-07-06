package com.uchump.prime.Core.Math.Primitive;

import java.util.function.Supplier;

import com.badlogic.gdx.math.MathUtils;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.A_I.iNumeric;
import com.uchump.prime.Core.Primitive.A_I.iNode;

public class aNumber extends Number implements iNode<Number>, CharSequence, iNumeric {

	public Number value = Float.NaN;
	public Supplier<Number> Value = () -> this.value;

	public aNumber() {

	}

	public aNumber(Number n) {

		this.value = n;
	}

	public aNumber(CharSequence s) {

	}

	public Number resolveToThis(Number n) {
		return N_Operator.resolveTo(n, this.value);
	}

	@Override
	public Number get() {
		return this.value;
	}

	@Override
	public void set(Number to) {
		this.value = to;
	}

	@Override
	public int intValue() {
		return this.value.intValue();
	}

	@Override
	public long longValue() {
		return this.value.longValue();
	}

	@Override
	public float floatValue() {
		return this.value.floatValue();
	}

	@Override
	public double doubleValue() {
		return this.value.doubleValue();
	}

	public Number numberValue() {
		return this.floatValue();
	}

	@Override
	public int compareTo(Object other) {
		// Number n resolveTo

		if (other instanceof Number) {
			Number n = (Number) other;
			if (MathUtils.isEqual(this.floatValue(), n.floatValue()))
				return 0;
			if (this.floatValue() > n.floatValue())
				return 1;
			if (this.floatValue() < n.floatValue())
				return -1;
		}

		return 0;
	}

	@Override
	public int length() {
		return this.value.toString().length();

	}

	@Override
	public char charAt(int index) {

		return this.value.toString().charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {

		return new aNumber(this.value.toString().subSequence(end, end));
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	public boolean isEqual(Number other, float epsilon) {
		return MathUtils.isEqual(this.floatValue(), other.floatValue(), epsilon);
	}

	public static class N_OP extends aNumber implements java.util.function.Function<Number, Number> {

		private java.util.function.Function<Object, Number> inner;

		public String label = "Operator";
		public String symbol = " ";

		public N_OP(Number initVal, java.util.function.Function<Object, Number> fn) {
			this.value = initVal;
			this.inner = fn;
		}

		@Override
		public Number apply(Number t) {
			// return this.inner.apply(t);
			if (this.inner instanceof java.util.function.BiFunction) {
				java.util.function.BiFunction Fn = (java.util.function.BiFunction) this.inner;
				return (Number) Fn.apply(this.value, Fn);
			} else {
				return this.inner.apply(t);
			}

		}

		@Override
		public int intValue() {
			return 0;
		}

		@Override
		public long longValue() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float floatValue() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double doubleValue() {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
