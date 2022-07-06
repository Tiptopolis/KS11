package com.uchump.prime.Core.Math;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.function.Predicate;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.uchump.prime.Core.DefaultResources;
import com.uchump.prime.Core.Math.Primitive.aNumber;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Utils._Lambda;
import com.uchump.prime.Core.Utils._Lambda.Function;

public abstract class N_Operator {

	// why tho?
	public static Function<Number, Number> Add = (Number a, Number b) -> {
		return add(a, b);
	};

	public static Number op(Number a, Number b) {
		if (a == null && b == null)
			return Float.NaN;
		if (a == null)
			return b;
		if (b == null)
			return a;

		return 0;
	}

	public static boolean isInt(Number n) {
		return instanceOf(Integer.class, Short.class, Long.class, Byte.class, Boolean.class).test(n);
	}

	public static boolean isFlt(Number n) {
		return instanceOf(Float.class, Double.class).test(n);
	}

	public static boolean isVec(Number n) {
		return instanceOf(aVector.class).test(n);
	}

	public static Number resolveTo(Number n, Number to) {
		if (instanceOf(Integer.class, Short.class, Long.class, Byte.class, Boolean.class).test(to))
			return n.intValue();

		if (instanceOf(Float.class, Double.class).test(to))
			return n.floatValue();
		return n;
	}

	public static Number resolve(Object n) {

		if (n == null || !(Number.class.isAssignableFrom(n.getClass()))) {
			return Float.NaN;
		}

		if (n instanceof Number)
			return resolve((Number) n);

		if (n instanceof CharSequence)
			return resolve((String) n);

		return Float.NaN;

	}

	public static Number resolve(String s) {

		String alph = DefaultResources.ENGLISH_LETTERS;
		for (int i = 0; i < alph.length(); i++) {
			if (s.contains(alph.substring(i, i + 1)))
				return Float.NaN;
		}

		if (s.contains("."))
			try {
				return Float.parseFloat(s);
			} catch (NumberFormatException e) {
				return Float.NaN;
			}
		else
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				return Float.NaN;
			}
	}

	public static Number resolve(Number n) {
		if (n == null)
			return Float.NaN;

		if ((Float.class.isAssignableFrom(n.getClass()))) {

			return n.floatValue();
		}

		if ((Integer.class.isAssignableFrom(n.getClass()))) {

			return n.intValue();
		}
		if ((Double.class.isAssignableFrom(n.getClass()))) {

			return n.doubleValue();
		}

		if ((Short.class.isAssignableFrom(n.getClass()))) {

			return n.shortValue();
		}

		if ((Long.class.isAssignableFrom(n.getClass()))) {

			return n.longValue();
		}

		if ((Byte.class.isAssignableFrom(n.getClass()))) {

			return n.byteValue();
		}

		return n;
	}

	public static <N extends Number> N[] resolveTo(Number[] Z, Number to) {
		N[] values = (N[]) new Number[Z.length];
		for (int i = 0; i < Z.length; i++)
			values[i] = (N) resolveTo(Z[i], to);

		return values;
	}

	//// [ADD]->(a+b)
	public static Number add(Number a, Number b) {
		if (a == null || b == null)
			return op(a, b);

		if (isInt(a))
			return a.intValue() + b.intValue();
		if (isFlt(a))
			return a.floatValue() + b.floatValue();
		if (isVec(a))
			return (Number) ((aVector) a).add(b);
		return 0;
	}

	
	
	//// [SUB]->(a-b)
	public static Number sub(Number a, Number b) {
		if (a == null || b == null)
			return op(a, b);
		if (isInt(a))
			return a.intValue() - b.intValue();
		if (isFlt(a))
			return a.floatValue() - b.floatValue();
		if (isVec(a))
			return (Number) ((aVector) a).sub(b);

		return 0;
	}

	//// [MUL]->(a*b)
	public static Number mul(Number a, Number b) {
		if (a == null || b == null)
			return op(a, b);
		if (isInt(a))
			return a.intValue() * b.intValue();
		if (isFlt(a))
			return a.floatValue() * b.floatValue();
		if (isVec(a))
			return (Number) ((aVector) a).mul(b);

		return 0;
	}

	//// [DIV]->(a/b)
	public static Number div(Number a, Number b) {
		if (a == null || b == null)
			return op(a, b);
		if (isInt(a))
			return a.intValue() / b.intValue();
		if (isFlt(a))
			return a.floatValue() / b.floatValue();
		if (isVec(a))
			return (Number) ((aVector) a).div(b);

		return 0;
	}

	public static Number pow(Number a, Number b) {
		if (a == null || b == null)
			return op(a, b);
		if (isInt(a))
			return (int) Math.pow(a.floatValue(), b.floatValue());
		if (isFlt(a))
			return (float) Math.pow(a.floatValue(), b.floatValue());
		if (isVec(a))
			return (aVector) ((aVector) a).pow(b);

		return a;
	}

	//// [ROOT]->(a-_/b)
	public static Number root(Number a, Number r) {

		if (r == null)
			r = 1;

		if (isInt(a))
			return resolveTo((pow(a, div(1f, r))), a);

		if (isFlt(a))
			return resolveTo((pow(a, div(1f, r))), a);

		if (isVec(a))
			return (Number) ((aVector) a).root(r);

		return 0;

	}

////[MAX]->([a>b=a]|[a<b=b]
	public static Number max(Number a, Number b) {
		if (b == null)
			b = Integer.MIN_VALUE;

		if (isInt(a))
			return Math.max(a.intValue(), b.intValue());

		if (isFlt(a))
			return Math.max(a.floatValue(), b.floatValue());

		if (isVec(a))
			return (Number) ((aVector) a).max(b);

		return 0;
	}

	public static Number maxOf(Number... n) {
		Number prev = n[0];

		for (Number N : n)
			prev = max(prev, N);
		return prev;
	}
	
	public static<N extends Number> Number maxOf(iCollection<N> n) {
		Number prev = n.get(0);

		for (Number N : n)
			prev = max(prev, N);
		return prev;
	}

////[MIN]->[a>b=b]|[a<b=a]
	public static Number min(Number a, Number b) {
		if (b == null)
			b = Integer.MIN_VALUE;

		if (isInt(a))
			return Math.min(a.intValue(), b.intValue());

		if (isFlt(a))
			return Math.min(a.floatValue(), b.floatValue());

		if (isVec(a))
			return (Number) ((aVector) a).min(b);

		return 0;
	}

	public static Number minOf(Number... n) {
		Number prev = n[0];

		for (Number N : n)
			prev = min(prev, N);
		return prev;
	}
	
	public static<N extends Number> Number minOf(iCollection<N> n) {
		Number prev = n.get(0);

		for (Number N : n)
			prev = min(prev, N);
		return prev;
	}
	
	public static Number minOf(aVector v)
	{
		return minOf(v.toList());
	}
	
	public static Number maxOf(aVector v)
	{
		return maxOf(v.toList());
	}

	//// [ABS]->(to positive)
	public static Number abs(Number a) {

		if (isInt(a))
			return (int) Math.abs(a.intValue());
		if (isFlt(a))
			return (float) Math.abs(a.floatValue());
		if (isVec(a))
			return ((aVector) a).abs();
		return 0;
	}

	//// [NEG]->(to negative)
	public static Number neg(Number a) {

		if (isInt(a))
			return (int) (Math.abs(a.intValue() * -1));
		if (isFlt(a))
			return (float) (Math.abs(a.floatValue() * -1f));
		if (isVec(a))
			return ((aVector) a).abs();
		return 0;
	}

	//// [CLAMP] -> [a>?>b] truncates to a min/max value
	public static Number clamp(Number value, Number min, Number max) {
		return max(min, min(max, value));
	}

	//// [FLOOR] -> establishes & applies a minimum value
	public static Number floor(Number n) {
		if (isVec(n))
			return ((aVector) n).floor();
		else
			return resolveTo(Math.floor(n.doubleValue()), n);
	}

	public static Number floor(Number a, Number b) {
		if (isVec(a))
			return ((aVector) a).floor((aVector) b);
		else
			return resolveTo(Math.floor(a.doubleValue()), b.floatValue());
	}

	public static Number floorOf(Number n) {
		if (n instanceof aVector) {
			return floor(((aVector) n).cpy());
		} else
			return floor(n);
	}

	//// [CEIL] -> establishes & applies a maximum value
	public static Number ceil(Number n) {
		if (isVec(n))
			return ((aVector) n).floor();
		else
			return resolveTo(Math.ceil(n.doubleValue()), n);
	}

	public static Number ceil(Number a, Number b) {
		if (isVec(a))
			return ((aVector) a).ceil((aVector) b);
		else
			return resolveTo(Math.ceil(a.doubleValue()), b.floatValue());
	}

	public static Number ceilOf(Number n) {
		if (n instanceof aVector) {
			return ceil(((aVector) n).cpy());
		} else
			return ceil(n);
	}

	// [FRACT] -> sets to the decimal part of a Number

	public static Number fract(Number n) {
		// 3.65 -> 0.65
		// Number r = sub(n, floor(n));

		Number N = n;
		Number f = floorOf(N);
		Number r = sub(n, f);

		return resolveTo(r, n);
	}

	//// [MOD]->(a%b) returns remainder after division
	public static Number mod(Number a, Number b) {
		if (a == null || b == null)
			return op(a, b);

		if (isEqual(b, 0))
			b = resolveTo(1, a);

		if (isInt(a))
			return a.intValue() % b.intValue();

		if (isFlt(a))
			return a.floatValue() % b.floatValue();

		if (isVec(a))
			return (Number) ((aVector) a).mod(b);

		return 0;
	}

	// [QUO]->(!(a%b)) andi-mod, returns integral portion after division
	public static Number quo(Number a, Number b) {
		return floor(div(a, b));
	}

	// [SIGN] -> sign of dividend

	public static Number sign(Number n) {
		return Math.signum(n.doubleValue());
	}

	public static Number toSign(Number a, Number b) {
		if (a == null || b == null)
			return op(a, b);

		if (isInt(a))
			return resolveTo(Math.signum(a.intValue() * (a.intValue() % b.intValue())), a);

		if (isFlt(a))
			return resolveTo(Math.signum(a.floatValue() * (a.floatValue() % b.floatValue())), a);

		if (isVec(a))
			return (Number) ((aVector) a).toSign(b);

		return 0;
	}

	public static Number lerp(Number min, Number max, Number val) {
		// return (1f / value) * min + max * value;

		Number vA = mul(div(1f, val), min);
		Number vB = mul(max, val);

		val = add(vA, vB);

		return val;
	}

	public static Number lerp(aValue.Range range, Number val) {
		return lerp(range.min, range.max, val);
	}

	// iLerp
	public static Number ilerp(Number min, Number max, Number val) {
		// (value - min) / (max - min);
		Number vA = sub(val.floatValue(), min); // at dst
		Number vB = sub(max, min); // total dst

		return resolveTo(div(vA, vB), val);
	}

	public static Number ilerp(aValue.Range range, Number val) {
		return ilerp(range.min, range.max, val);
	}

	// sLerp

	//
	public static Number remap(Number minOld, Number maxOld, Number minNew, Number maxNew, Number value) {
		Number f = ilerp(minOld, maxOld, value);
		return lerp(minNew, maxNew, f);
	}

	public static Number remap(aValue.Range oldRange, aValue.Range newRange, Number value) {
		return remap(oldRange.min, oldRange.max, newRange.min, newRange.max, value);
	}

	public static Number pin(Number value, Number n) {
		// sets the value to a floating pt at n-decimal places

		// if aVector
		// V->CSV->V || CSV->V->CSV

		return toPrecisionOf(n, value);
	}

	public static Number sin(Number theta) {

		if (!(theta instanceof aVector)) {
			float t = theta.floatValue();
			return resolveTo(Math.sin(t), theta);
		} else {
			aVector T = (aVector) theta;
			for (int i = 0; i < T.size(); i++) {
				T.setAt(i, sin(T.get(i)));
			}
			return T;
		}

	}

	public static Number cos(Number theta) {

		if (!(theta instanceof aVector)) {
			float t = theta.floatValue();
			return resolveTo(Math.cos(t), theta);
		} else {
			aVector T = (aVector) theta;
			for (int i = 0; i < T.size(); i++) {
				T.setAt(i, cos(T.get(i)));
			}
			return T;
		}
	}

	public static Number tan(Number theta) {

		if (!(theta instanceof aVector)) {
			float t = theta.floatValue();
			return resolveTo(Math.tan(t), theta);
		} else {
			aVector T = (aVector) theta;
			for (int i = 0; i < T.size(); i++) {
				T.setAt(i, tan(T.get(i)));
			}
			return T;
		}
	}

	public static Number atan(Number theta) {

		if (!(theta instanceof aVector)) {
			float t = theta.floatValue();
			return resolveTo(Math.atan(t), theta);
		} else {
			aVector T = (aVector) theta;
			for (int i = 0; i < T.size(); i++) {
				T.setAt(i, atan(T.get(i)));
			}
			return T;
		}
	}

	public static Number sum(Number... other) {

		Number N = other[0];
		if (other.length == 1)
			return N;

		for (int i = 1; i < other.length; i++) {
			Number o = other[i];
			N = N_Operator.add(N, o);
		}

		return N;
	}

	public static Number dif(Number... other) {
		Number N = other[0];
		if (other.length == 1)
			return N;

		for (int i = 1; i < other.length; i++)
			N = N_Operator.sub(N, other[i]);

		return N;
	}

	public static Number mag(Number... other) {
		Number N = other[0];
		if (other.length == 1)
			return N;

		for (int i = 1; i < other.length; i++)
			N = N_Operator.mul(N, other[i]);

		return N;
	}

	public static Number quo(Number... other) {
		Number N = other[0];
		if (isEqual(N, 0))
			N = 1;
		if (other.length == 1)
			return N;

		for (int i = 1; i < other.length; i++) {
			Number O = other[i];
			if (isEqual(O, 0))
				O = 1;
			N = N_Operator.div(N, O);
		}

		return N;
	}

	public static boolean isEqual(Number a, Number b, float tollerance) {
		if (b == null)
			return false;
		if (a instanceof Integer)
			return a.intValue() == b.intValue();
		if (a instanceof aVector)
			return ((aVector) a).isEqual(b, tollerance);
		if (isDecimal(a))
			return MathUtils.isEqual(a.floatValue(), b.floatValue(), tollerance);

		return false;
	}

	public static boolean isEqual(Number a, Number b) {
		return isEqual(a, b, MathUtils.FLOAT_ROUNDING_ERROR);
	}

	public static boolean isEqual(Vector3 a, Vector3 b, float epsilon) {
		if (MathUtils.isEqual(a.x, b.x, epsilon) && MathUtils.isEqual(a.y, b.y, epsilon)
				&& MathUtils.isEqual(a.z, b.z, epsilon))
			return true;
		else
			return false;
	}

	private static void _UTIl_() {

	}

	public static Number[] scanFrom(Object[] other) {
		Number[] Result = new Number[other.length];

		for (int i = 0; i < other.length; i++) {

			Number N = resolve(other[i]);
			Result[i] = N;

		}

		return Result;
	}

	public static Number[] toDigits(Number N) {
		Number[] Result = new Number[N.intValue()];
		for (int i = 0; i <= Result.length; i++) {
			Result[i] = i;
		}

		return Result;
	}

	public static boolean isDecimal(Number n) {

		if (n instanceof aNumber) {
			aNumber N = (aNumber) n;
			return isDecimal(N.get());
		}

		if (n instanceof Integer)
			return false;
		else
			return true;
	}

	public static int getPrecision(Number n) {
		if (n instanceof Integer)
			return n.intValue();

		int d = ("" + n).lastIndexOf('.');
		int l = ("" + n).length() - d;
		// Log(d+" __ "+ l + " :: " + n);
		return l;
	}

	public static Number toPrecisionOf(Number val, Number of) {
		int dec = 0;
		if (of instanceof Integer)
			dec = of.intValue();
		else
			dec = getPrecision(of.floatValue()) - 1;

		// Log(of+">>"+dec);

		return Maths.round(val.floatValue(), dec);

	}

	public static boolean lessThan(Number a, Number b) {
		if (AllMatch(instanceOf(aVector.class), a, b)) {

		}

		return false;
	}
}
