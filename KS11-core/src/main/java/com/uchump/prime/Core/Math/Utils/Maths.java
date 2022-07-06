package com.uchump.prime.Core.Math.Utils;

import static com.uchump.prime.Core.uAppUtils.*;

import java.math.BigDecimal;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.uchump.prime.Core.Math.Primitive.aVector;

public class Maths {

	// EXP4j Expressions
	// fill array with permutations of inputs

	// so #[]{1,2,3}

	// calculatePermutations(sVector/Array inputConstants)
	// pass in an nSized Vector/Array
	// returns Array containing possible arrangements

	// base of the natural logarithm
	// has a limit of(1+1/n)^n as n approaches infinity
	// arises in compound interest
	// Sum of the infinite series
	// Sigma(0-infinity)[for(int n = 0; n < Infinity; n++]
	// {
	// 1/Factorial(n) n!
	// }
	// irrational number
	public static double e = 2.71828f;
	public static double EulersNumber = e;

	public static double E(int n) {
		float result = 0;
		result = 1 / factorial(n);
		return result;
	}

	public static int factorial(int n) {
		// Factorial of n = n! in normal math
		// recursion lol
		if (n == 0)
			return 1;
		else
			return (n * factorial(n - 1));
	}

	public static float roundToNearest(float x, float num) {
		// return x * (num / x);
		return num * (Math.round((float) x / num));
		// 5*(Math.ceil(Math.abs(number/5))); //upper value nearest multiple of 5
		// 5*(Math.floor(Math.abs(number/5)));//lower value nearest multiple of 5
	}

	@SuppressWarnings("deprecation")
	public static BigDecimal round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd;
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

	public static <T> void printAllRecursive(int n, T[] elements, char delimiter) {

		if (n == 1) {
			printArray(elements, delimiter);
		} else {
			for (int i = 0; i < n - 1; i++) {
				printAllRecursive(n - 1, elements, delimiter);
				if (n % 2 == 0) {
					swap(elements, i, n - 1);
				} else {
					swap(elements, 0, n - 1);
				}
			}
			printAllRecursive(n - 1, elements, delimiter);
		}
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

	private static <T> void swap(T[] input, int a, int b) {
		T tmp = input[a];
		input[a] = input[b];
		input[b] = tmp;
	}

	// Mapping function for doubles (standard only for floats)
	public static double map(double val, double oMin, double oMax, double nMin, double nMax) {
		return ((val - oMin) / (oMax - oMin)) * (nMax - nMin) + nMin;
	}

	// Mapping function for doubles (standard only for floats)
	public static float map(float val, float oMin, float oMax, float nMin, float nMax) {
		return ((val - oMin) / (oMax - oMin)) * (nMax - nMin) + nMin;
	}

	// Mapping function for doubles (standard only for floats)
	public static int map(int val, float oMin, float oMax, float nMin, float nMax) {
		return (int) (((val - oMin) / (oMax - oMin)) * (nMax - nMin) + nMin);
	}

	///////////
	//
	public static float lerp(float min, float max, float value) {
		return (1f / value) * min + max * value;
	}

	public static Vector3 lerp(Vector3 min, Vector3 max, Vector3 value) {
		float x = lerp(min.x, max.x, value.x);
		float y = lerp(min.y, max.y, value.y);
		float z = lerp(min.z, max.z, value.z);

		return new Vector3(x, y, z);
	}

	// value is ?% of (min,max);
	public static float inverseLerp(float min, float max, float value) {
		return (value - min) / (max - min);
	}

	public static Vector3 inverseLerp(Vector3 min, Vector3 max, Vector3 value) {
		float x = inverseLerp(min.x, max.x, value.x);
		float y = inverseLerp(min.y, max.y, value.y);
		float z = inverseLerp(min.z, max.z, value.z);

		return new Vector3(x, y, z);
	}

	public static float remap(float nMin, float nMax, float oMin, float oMax, float value) {
		float t = inverseLerp(oMin, oMax, value);
		return lerp(nMin, nMax, t);
	}

	public static Vector3 remap(Vector3 nMin, Vector3 nMax, Vector3 oMin, Vector3 oMax, Vector3 value) {
		float x = remap(nMin.x, nMax.x, oMin.x, oMax.x, value.x);
		float y = remap(nMin.y, nMax.y, oMin.y, oMax.y, value.y);
		float z = remap(nMin.z, nMax.z, oMin.z, oMax.z, value.z);

		return value.set(x, y, z);
	}

	public static Vector3 abs(Vector3 vector) {
		float x = Math.abs(vector.x);
		float y = Math.abs(vector.y);
		float z = Math.abs(vector.z);
		vector.set(x, y, z);
		return vector;
	}

	public static float min(float a, float b) {
		if (a > b)
			return b;
		else
			return a;
	}

	public static float max(float a, float b) {
		if (a > b)
			return a;
		else
			return b;
	}

	public float min(float... values) {
		float result = values[0];
		for (int i = 0; i < values.length; i++) {
			if (values[i] < result)
				result = values[i];
		}
		return result;
	}

	public float max(float... values) {
		float result = values[0];
		for (int i = 0; i < values.length; i++) {
			if (values[i] > result)
				result = values[i];
		}
		return result;
	}

	public Vector2 minIndex(float... values) {
		Vector2 result = new Vector2(-1, Float.NaN);

		for (int i = 0; i < values.length; i++) {
			if (values[i] > result.y)
				result = new Vector2(i, values[i]);
		}

		return result;

	}

	// x component is index of the value in given array
	public Vector2 maxIndex(float... values) {
		Vector2 result = new Vector2(-1, Float.NaN);

		for (int i = 0; i < values.length; i++) {
			if (values[i] < result.y)
				result = new Vector2(i, values[i]);
		}

		return result;

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

	private static <T> void printArray(T[] input, char d) {
		System.out.print('\n');

		//
		// substring
		//
		for (int i = 0; i < input.length; i++) {
			System.out.print(input[i]);
		}

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

	// input 3 side lengths, get 3 cosines
	public static Vector3 cosX(float a, float b, float c) {
		// theta is actually delta a-b mapped to a range of 0-2pi?
		Vector3 result = new Vector3();
		float cosC = ((a * a) + (b * b) - (c * c)) / (2 * (a * b));
		float cosA = ((b * b) + (c * c) - (a * a)) / (2 * (b * c));
		float cosB = ((c * c) + (a * a) - (b * b)) / (2 * (c * a));

		result.set(cosA, cosB, cosC);
		return result;
	}

}
