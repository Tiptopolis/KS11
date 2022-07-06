package com.uchump.prime.Core.Math.Utils;

import static com.uchump.prime.Core.uAppUtils.*;

import java.math.BigDecimal;

import com.badlogic.gdx.math.MathUtils;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.aNumber;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Math.Utils.aVectorUtils;
import com.uchump.prime.Core.Primitive.aValue;

public abstract class aMaths {

	public static boolean isEqual(Number a, Number b) {
		return isEqual(a, b, MathUtils.FLOAT_ROUNDING_ERROR);
	}

	public static boolean isEqual(Number a, Number b, float tollerance) {
		return N_Operator.isEqual(a, b, tollerance);
	}

	public static Number roundToNearest(Number x, Number num) {
		// return num * (Math.round((float) x / num));
		float r = num.floatValue() * (Math.round(x.floatValue() / num.floatValue()));
		return N_Operator.resolveTo(r, num);
	}

	public static Number toPrecision(Number d, int dec) {
		float n = Maths.round(d.floatValue(), dec).floatValue();
		return N_Operator.resolveTo(n, d);
	}

	public static Number toPrecision(Number d, Number dec) {
		return toPrecision(d, getPrecision(dec));
	}

	public static int getPrecision(Number n) {
		if (instanceOf(Integer.class, Long.class, Short.class).test(n))
			return n.intValue();
		if (instanceOf(Float.class, Double.class).test(n))
			return ("" + n.doubleValue()).split("/.")[0].length();
		if (instanceOf(aNumber.class).test(n))
			return getPrecision(((aNumber) n).numberValue());

		return 0;
	}

	public static boolean isPositive(Number n) {
		if (n.floatValue() >= 0)
			return true;
		else
			return false;
	}

	//////
	// so X is +/-N
	public static boolean isWithin(Number is, aValue.Range range) {
		return range.contains(is);
	}

	public static boolean isWithin(Number is, aValue.Range range, Number of) {
		// -1/1 means within +-1 of
		if (of instanceof aVector)
			of = ((aVector) of).cpy();
		Number max = N_Operator.add(range.max, of);
		Number min = N_Operator.sub(of, N_Operator.abs(range.min));
		return new aValue.Range(min, max).contains(is);
	}

	public static Number min(aVector... vects) {

		int iterSize = aVectorUtils.shortestSize(vects);
		Number[] out = new Number[iterSize];
		for (int i = 0; i < iterSize; i++) {
			Number min = Integer.MAX_VALUE;
			for (int j = 0; j < vects.length; j++) {

				min = min(min.floatValue(), vects[j].get(i).floatValue());
			}
			out[i] = min;
		}

		return new aVector(out);
	}

	// (6,2,4,9,5) -> 2
	public static Number min(aVector vals) {
		return min(vals.toArray());
	}

	public static Number min(Number... vals) {
		Number min = Integer.MAX_VALUE;

		for (int i = 0; i < vals.length; i++) {

			min = N_Operator.min(min.floatValue(), vals[i]);

		}

		return min;
	}

	public static Number max(aVector... vects) {
		int iterSize = aVectorUtils.shortestSize(vects);
		Number[] out = new Number[iterSize];
		for (int i = 0; i < iterSize; i++) {
			Number max = 0;
			for (int j = 0; j < vects.length; j++) {

				max = max(max.floatValue(), vects[j].get(i).floatValue());
			}
			out[i] = max;
		}

		return new aVector(out);
	}

	public static Number max(aVector vals) {
		return max(vals.toArray());
	}

	public static Number max(Number... vals) {
		Number max = 0f;

		for (int i = 0; i < vals.length; i++)
			max = N_Operator.max(max, vals[i]);

		return max;
	}

	public static boolean isEven(int n) {
		// Least Significant Bit of an odd number is always set (1)
		// if ((n.byteValue() ^ 1) == n.byteValue() + 1) {
		// return (true);
		// } else
		// return (false);
		return ((n & 1) == 1) ? false : true;
	}

	public static boolean isOdd(int n) {
		// Least Significant Bit of an odd number is always set (1)
		// if ((n.byteValue() ^ 1) == n.byteValue() + 1) {
		// return (true);
		// } else
		// return (false);
		return ((n & 1) == 1) ? true : false;
	}

	public static Number map(Number val, Number oMin, Number oMax, Number nMin, Number nMax) {
		// return ((val - oMin) / (oMax - oMin)) * (nMax - nMin) + nMin;

		Number a = N_Operator.sub(val, oMin);
		Number b = N_Operator.sub(oMax, oMin);
		Number c = N_Operator.sub(nMax, nMin);
		Number ab = N_Operator.div(a, b);
		Number abc = N_Operator.mul(ab, c);
		Number d = nMin;

		Number res = N_Operator.add(abc, d);

		return res;
	}

	// map theta from 0-(PI/2)
	public static Number remap(Number value, Number oMin, Number oMax, Number nMin, Number nMax) {

		// return ((val - oMin) / (oMax - oMin)) * (nMax - nMin) + nMin;

		return lerp(nMin, nMax, (inverseLerp(oMin, oMax, value)));
	}

	// returns value@% of range min->max
	// 0.5, or 50%, of range(0-5) is 2.5
	// MAPS FLOAT TO INT
	public static Number lerp(Number min, Number max, Number value) {
		float v = (1f / value.floatValue()) * min.floatValue() + max.floatValue() * value.floatValue();
		if (min.floatValue() < 0) {
			Number d = N_Operator.sub(max, min);
			// remap(value,min,max,0,d);
			// get lerp of remapped val
			Number N = remap(value, min, max, 0, d);
			return lerp(0, d, N);
		} else
			return N_Operator.resolveTo(v, value);
	}

	// returns %@value of range min->max
	// 0.5 of range(0-5) is 0.1, or 10%
	// MAPS INT TO FLOAT
	public static Number inverseLerp(Number min, Number max, Number value) {
		// return (value - min) / (max - min);
		if (value instanceof aVector)
			return aVectorUtils.inverseLerp(min, max, (aVector) value);

		Number a = N_Operator.sub(value.floatValue(), min.floatValue());
		Number b = N_Operator.sub(max.floatValue(), min.floatValue());
		Number V = N_Operator.div(a, b);

		return N_Operator.resolveTo(V, 1f);
	}

	public boolean inRange(Number x, Number a, Number b) {
		return x.floatValue() >= a.floatValue() && x.floatValue() <= b.floatValue();
	}

	public Number smoothstep(Number x, Number a, Number b) {
		x = x.floatValue() * x.floatValue() * (3.0 - 2.0 * x.floatValue());
		return x.floatValue() * (b.floatValue() - a.floatValue()) + a.floatValue();
	}

	public Number smootherstep(Number x, Number a, Number b) {
		x = x.floatValue() * x.floatValue() * x.floatValue() * (x.floatValue() * (x.floatValue() * 6 - 15) + 10);
		return x.floatValue() * (b.floatValue() - a.floatValue()) + a.floatValue();
	}

	public static aVector compareTo(aVector a, aVector b) {
		Number[] result = new Number[(int) Maths.min(a.size(), b.size())];
		for (int i = 0; i < a.size() && i < b.size(); i++) {
			// Log(">"+i);
			result[i] = (compareTo(a.get(i), b.get(i)));
		}
		return new aVector(result);
	}

	public static int compareTo(Number a, Number b) {
		if (MathUtils.isEqual(a.floatValue(), b.floatValue()))
			return 0;
		if (a.floatValue() > b.floatValue())
			return 1;
		if (a.floatValue() < b.floatValue())
			return -1;

		return 0;
	}

	public static int sign(Number n) {
		return compareTo(n, 0);
	}

	// -mod?
	public static Number signMod(Number n, Number mod) {
		int s = sign(n);

		Number m = N_Operator.mod(N_Operator.abs(n), mod);
		return N_Operator.mul(m, s);

	}

	// for lists n such
	// mod index by size, if negative, gets size-index modded to range
	// 0->360 >>%359(size-1); -1 yields 359 (360-1); range reclusive 360 >> 0-359
	// q-mod, address mod, etc
	public static Number arrMod(Number n, Number mod) {
		if (compareTo(n, 0) < 0) {
			int s = sign(n);
			// abs mod - abs n
			Number N = N_Operator.abs(n);
			n = N_Operator.mod(N, mod);
			// mod - n
			n = N_Operator.sub(mod, n);
			n = N_Operator.mul(n, s);
			return N_Operator.abs(n);
		} else
			return N_Operator.mod(n, mod);
	}

	// doubls depth times
	public static Number exp(Number n, int depth) {
		for (int i = 0; i < depth; i++)
			n = N_Operator.mul(n, 2);
		return n;
	}
	public static Number exp(Number n, int depth, Number by) {
		for (int i = 0; i < depth; i++)
			n = N_Operator.mul(n, by);
		return n;
	}

	public static int factorial(int n) {
		// Factorial of n = n! in normal math
		// recursion lol
		if (n == 0)
			return 1;
		else
			return (n * factorial(n - 1));
	}

	public static float factorial(float n) {
		// Factorial of n = n! in normal math
		// recursion lol
		if (isEqual(n, 0f))
			return 1f;
		else
			return (n * factorial(n - 1f));
	}

	public static Number factorial(Number n) {
		// Factorial of n = n! in normal math
		// recursion lol
		if (isEqual(n.floatValue(), 0f))
			return 1;
		else
			return N_Operator.mul(n, factorial(N_Operator.sub(n, 1)));
	}

	// greatest common divisor
	public static int gcd(int p, int q) {
		if (q == 0)
			return p;
		if (p == 0)
			return q;

		// p and q even
		if ((p & 1) == 0 && (q & 1) == 0)
			return gcd(p >> 1, q >> 1) << 1;

		// p is even, q is odd
		else if ((p & 1) == 0)
			return gcd(p >> 1, q);

		// p is odd, q is even
		else if ((q & 1) == 0)
			return gcd(p, q >> 1);

		// p and q odd, p >= q
		else if (p >= q)
			return gcd((p - q) >> 1, q);

		// p and q odd, p < q
		else
			return gcd(p, (q - p) >> 1);
	}

	public static int ncr(int n, int r) {
		return factorial(n) / (factorial(n - r) * factorial(r));
	}

	public static int compare(Number a, Number b) {
		if (a instanceof Integer) {
			int A = a.intValue();
			int B = b.intValue();
			if (A < B)
				return -1;
			if (A == B)
				return 0;
			if (A > B)
				return 1;
		} else {
			float A = a.floatValue();
			float B = b.floatValue();
			if (A < B)
				return -1;
			if (MathUtils.isEqual(A, B))
				return 0;
			if (A > B)
				return 1;
		}

		return 0;
	}

	////////////////
	private static void _N_() {

	}

	public static aVector[] PascalTri(int n) {
		return PascalTriL(n);
	}

	public static aVector[] PascalTriC(int n) {
		aVector[] res = new aVector[n + 1];
		int i, j;

		for (i = 0; i <= n; i++) {
			aVector nv = new aVector();
			for (j = 0; j <= n - i; j++) {
				nv.append(0);
			}
			for (j = 0; j <= i; j++) {
				nv.append(ncr(i, j));
			}
			res[i] = nv;
		}

		int maxD = aVectorUtils.longestSize(res);
		for (aVector v : res) {
			int s = v.size();
			int f = ((maxD - s) / 2);
			for (i = 0; i < f; i++)
				v.append(0);
		}

		return res;
	}

	public static aVector[] PascalTriL(int n) {
		aVector[] res = new aVector[n + 1];
		int i, j;

		for (i = 0; i <= n; i++) {
			aVector nv = new aVector();

			for (j = 0; j <= i; j++) {
				nv.append(ncr(i, j));
			}
			res[i] = nv;
		}
		return res;
	}

	public static aVector[] PascalTriR(int n) {
		aVector[] res = new aVector[n + 1];
		int i, j;

		for (i = 0; i <= n; i++) {
			aVector nv = new aVector();
			for (j = 0; j <= n - i; j++) {
				nv.append(0);
			}
			for (j = 0; j <= i; j++) {
				nv.append(ncr(i, j));
			}
			res[i] = nv;
		}
		return res;
	}

}
