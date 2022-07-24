package com.uchump.prime.Core.Math;

import static com.uchump.prime.Core.uAppUtils.*;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.uchump.prime.Core.Math.Primitive.aMatrix;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.Maths;

public abstract class V_Operator {

	//// [ADD]->(a+b)
	public static aVector add(aVector a, Number b) {
		if (b instanceof aVector)
			return add(a, (aVector) b);
		else
			for (int i = 0; i < a.size(); i++)
				a.setAt(i, N_Operator.add(a.get(i), b));

		return a;
	}

	public static Number add(Number a, aVector b) {
		if (a instanceof aVector)
			return add((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.add(a, b.get(i));
		return a;
	}

	public static aVector add(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());

		for (int i = 0; i < s; i++) {
			Number r = N_Operator.add(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	public static Number add(aVector v, aMatrix m) {
		for (aVector N : m.getComponentData())
			v.add(N);

		return v;
	}

	//// [SUB]->(a-b)
	public static aVector sub(aVector a, Number b) {
		if (b instanceof aVector)
			return sub(a, (aVector) b);
		else
			for (int i = 0; i < a.size(); i++)
				a.setAt(i, N_Operator.sub(a.get(i), b));

		return a;
	}

	public static Number sub(Number a, aVector b) {
		if (a instanceof aVector)
			return sub((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.sub(a, b.get(i));
		return a;
	}

	public static aVector sub(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.sub(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	public static Number sub(aVector v, aMatrix m) {
		for (aVector N : m.getComponentData())
			v.sub(N);
		return v;
	}

	//// [MUL]->(a*b)
	public static aVector mul(aVector a, Number b) {
		if (b instanceof aVector)
			return mul(a, (aVector) b);
		else
			for (int i = 0; i < a.size(); i++)
				a.setAt(i, N_Operator.mul(a.get(i), b));

		return a;
	}

	public static Number mul(Number a, aVector b) {
		if (a instanceof aVector)
			return mul((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.mul(a, b.get(i));
		return a;
	}

	public static aVector mul(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.mul(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	public static Number mul(aVector v, aMatrix m) {
		for (aVector N : m.getComponentData())
			v.mul(N);
		return v;
	}

	//// [DIV]->(a/b)
	public static aVector div(aVector a, Number b) {
		if (b instanceof aVector)
			return div(a, (aVector) b);
		else
			for (int i = 0; i < a.size(); i++)
				a.setAt(i, N_Operator.div(a.get(i), b));

		return a;
	}

	public static Number div(Number a, aVector b) {
		if (a instanceof aVector)
			return div((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.div(a, b.get(i));
		return a;
	}

	public static aVector div(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.div(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	public static Number div(aVector v, aMatrix m) {
		for (aVector N : m.getComponentData())
			v.div(N);
		return v;
	}

////[POW]->(a^b)
	public static aVector pow(aVector a, Number p) {

		for (int i = 0; i < a.size(); i++)
			a.setAt(i, N_Operator.pow(a.get(i), p));

		return a;
	}

	public static Number pow(Number a, aVector p) {
		if (a instanceof aVector)
			return pow((aVector) a, p);
		else
			for (int i = 0; i < p.size(); i++)
				a = N_Operator.pow(a, p.get(i));
		return a;
	}

	public static aVector pow(aVector a, aVector p) {
		int s = Math.min(a.size(), p.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.pow(a.get(i), p.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	//// [ROOT]->(a-_/b)
	public static aVector root(aVector a, Number p) {

		for (int i = 0; i < a.size(); i++)
			a.setAt(i, N_Operator.root(a.get(i), p));

		return a;
	}

	public static Number root(Number a, aVector p) {
		if (a instanceof aVector)
			return root((aVector) a, p);
		else
			for (int i = 0; i < p.size(); i++)
				a = N_Operator.root(a, p.get(i));
		return a;
	}

	public static aVector root(aVector a, aVector p) {
		int s = Math.min(a.size(), p.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.root(a.get(i), p.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	//// [MAX]->()->[a>b=a][a<b=b]
	public static aVector max(aVector a, Number b) {
		for (int i = 0; i < a.size(); i++)
			a.setAt(i, N_Operator.max(a.get(i), b));

		return a;
	}

	public static Number max(Number a, aVector b) {
		if (a instanceof aVector)
			return max((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.max(a, b.get(i));
		return a;
	}

	public static aVector max(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.max(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	//// [MIN]->()->[a>b=b][a<b=a]
	public static aVector min(aVector a, Number b) {
		for (int i = 0; i < a.size(); i++)
			a.setAt(i, N_Operator.min(a.get(i), b));

		return a;
	}

	public static Number min(Number a, aVector b) {
		if (a instanceof aVector)
			return min((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.min(a, b.get(i));
		return a;
	}

	public static aVector min(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.min(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

////[FLOOR] -> establishes & applies a minimum integral value

	public static aVector floor(aVector a, Number b) {
		for (int i = 0; i < a.size(); i++)
			a.setAt(i, N_Operator.floor(a.get(i), b));

		return a;
	}

	public static Number floor(Number a, aVector b) {
		if (a instanceof aVector)
			return floor((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.floor(a, b.get(i));
		return a;
	}

	public static aVector floor(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.floor(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	public static aVector floorOf(aVector a) {
		for (int i = 0; i < a.size(); i++) {
			Number b = a.get(i);
			a.setAt(i, N_Operator.floorOf(b));
		}
		return a;
	}

	//// [CEIL] -> establishes & applies a maximum integral value
	public static aVector ceil(aVector a, Number b) {
		for (int i = 0; i < a.size(); i++)
			a.setAt(i, N_Operator.ceil(a.get(i), b));

		return a;
	}

	public static aVector ceilOf(aVector a) {
		for (int i = 0; i < a.size(); i++) {
			Number b = a.get(i);
			a.setAt(i, N_Operator.ceilOf(b));
		}
		return a;
	}

	public static Number ceil(Number a, aVector b) {
		if (a instanceof aVector)
			return ceil((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.ceil(a, b.get(i));
		return a;
	}

	public static aVector ceil(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.ceil(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	// [FRACT] -> sets to the decimal part of a Number
	public static aVector fract(aVector a) {
		// 3.65 -> 0.65
		// Number r = sub(n, floor(n));

		for (int i = 0; i < a.size(); i++) {
			Number b = a.get(i);
			a.setAt(i, N_Operator.fract(b));
		}

		return a;
	}

	//// [MOD]->(a%b)
	public static aVector mod(aVector a, Number p) {

		for (int i = 0; i < a.size(); i++)
			a.setAt(i, N_Operator.mod(a.get(i), p));

		return a;
	}

	public static Number mod(Number a, aVector p) {
		if (a instanceof aVector)
			return mod((aVector) a, p);
		else
			for (int i = 0; i < p.size(); i++)
				a = N_Operator.mod(a, p.get(i));
		return a;
	}

	public static aVector mod(aVector a, aVector p) {
		int s = Math.min(a.size(), p.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.mod(a.get(i), p.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	//// [QUO]->(!(a%b)) andi-mod, returns integral portion after division
	public static aVector quo(aVector a, Number b) {
		for (int i = 0; i < a.size(); i++)
			a.setAt(i, N_Operator.quo(a.get(i), b));

		return a;
	}

	public static Number quo(Number a, aVector b) {
		if (a instanceof aVector)
			return quo((aVector) a, b);
		else
			for (int i = 0; i < b.size(); i++)
				a = N_Operator.quo(a, b.get(i));
		return a;
	}

	public static aVector quo(aVector a, aVector b) {
		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number r = N_Operator.quo(a.get(i), b.get(i));
			a.setAt(i, r);
		}
		return a;
	}

	//// [SIGN] -> sign of dividend
	public static aVector toSign(aVector a, Number b) {

		for (int i = 0; i < a.size(); i++) {
			Number n = a.get(i);
			n = N_Operator.toSign(n, b);
			a.setAt(i, n);
		}
		return a;
	}

	public static Number toSign(Number a, aVector b) {
		Number B = b.sum();
		return N_Operator.toSign(a, b);
	}

	public static aVector toSign(aVector a, aVector b) {

		int s = Math.min(a.size(), b.size());
		for (int i = 0; i < s; i++) {
			Number n = a.get(i);
			n = N_Operator.toSign(n, b.get(i));
			a.setAt(i, n);
		}
		return a;
	}

	//// [DOT] -> inner-product
	public static float dot(aVector a, Number b) {
		int s = a.size();
		aVector B = (aVector) new aVector().filled(s, b);
		return dot(a, B);
	}

	public static float dot(aVector a, aVector b) {
		float result = 0;
		for (int i = 0; i < a.size(); i++) {
			float apnd = 0;
			float at = a.get(i).floatValue();
			if (i < b.size()) {
				apnd = b.get(i).floatValue();
			}
			result += at * apnd;
		}

		return result;
	}

	public static float dot2(aVector a) {
		return dot(a, a);
	}

	public static float ndot(aVector a, aVector b) {
		float result = 0;
		for (int i = 0; i < a.size(); i++) {
			float apnd = 0;
			float at = a.get(i).floatValue();
			if (i < b.size()) {
				apnd = b.get(i).floatValue();
			}
			result -= at * apnd;
		}

		return result;
	}

	//// [CRS] - outter-product

	public static aVector crs2d(aVector a, aVector b) {

		if (b.size() == 1) {
			Number N = N_Operator.resolveTo(b.get(0), a.value);
			b = new aVector(0, N, N);
		}

		if (b.size() == 2) {
			b = b.cpy();
			b.append(0);
		}
		a.append(0);

		return a.crs(b).collapseLeft();

	}

	public static aVector ncrs2d(aVector a, aVector b) {
		if (b.size() == 1) {
			Number N = N_Operator.resolveTo(b.get(0), a.value);
			b = new aVector(0, N, N);
		}

		if (b.size() == 2) {
			b = b.cpy();
			b.append(0);
		}
		a.append(0);

		return a.ncrs(b).collapseLeft();
	}

	//outter product
	public static aVector crs(aVector a, aVector<Number> b) {

		// I=(A*B-B*A)
		// forEach: this[i] = ( ( this.i|A * other.i|B ) - ( this.i|B * other.i|A ) )
		// add trailing 0s for 2d & under

		if (a.size() == 2)
			return a.crs2d(b);

		aVector<Number> result = new aVector<Number>();
		result.value = a.value;

		boolean r = false;
		Number at = N_Operator.resolveTo(a.value, 0);
		for (int i = 0; i < a.size(); i++) {

			r = Maths.isEven(i);
			for (int A = 0; A < a.size() - 1; A++)
				for (int B = a.size() - 1; B > 0; B--)
					if (A != i && B != i && A != B) {
						// if (Maths.isEven(i))
						// Log(i + " [[ " + a + " : " + b);
						// else
						// Log(i + " [[ " + b + " : " + a);
						Number A1 = a.get(A);
						Number A2 = b.get(B);
						Number B1 = a.get(B);
						Number B2 = b.get(A);
						// Log(" {{" + A1 + "*" + B1 + "}-{" + A2 + "*" + B2 + "}={" +
						// N_Operator.mul(A2, B2) + "}-{"
						// + N_Operator.mul(A1, B1) + "}");

						if (r)
							at = N_Operator.sub(N_Operator.mul(A1, A2), N_Operator.mul(B2, B1));
						else
							at = N_Operator.sub(N_Operator.mul(B1, B2), N_Operator.mul(A2, A1));
					}

			result.setAt(i, at);

		}
		a.set(result);

		a.value = N_Operator.resolveTo(a.value, at);
		return a;
	}

	public static aVector ncrs(aVector a, aVector<Number> b) {

		// I=(A*B-B*A)
		// forEach: this[i] = ( ( this.i|A * other.i|B ) - ( this.i|B * other.i|A ) )
		// add trailing 0s for 2d & under

		if (a.size() == 2)
			return a.ncrs2d(b);

		aVector<Number> result = new aVector<Number>();
		result.value = a.value;

		boolean r = false;
		Number at = N_Operator.resolveTo(a.value, 0);
		for (int i = 0; i < a.size(); i++) {

			r = Maths.isEven(i);
			for (int A = 0; A < a.size() - 1; A++)
				for (int B = a.size() - 1; B > 0; B--)
					if (A != i && B != i && A != B) {
						// if (Maths.isEven(i))
						// Log(i + " [[ " + a + " : " + b);
						// else
						// Log(i + " [[ " + b + " : " + a);
						Number A1 = a.get(A);
						Number A2 = b.get(B);
						Number B1 = a.get(B);
						Number B2 = b.get(A);
						// Log(" {{" + A1 + "*" + B1 + "}-{" + A2 + "*" + B2 + "}={" +
						// N_Operator.mul(A2, B2) + "}-{"
						// + N_Operator.mul(A1, B1) + "}");

						if (r)
							at = N_Operator.add(N_Operator.mul(A1, A2), N_Operator.mul(B2, B1));
						else
							at = N_Operator.add(N_Operator.mul(B1, B2), N_Operator.mul(A2, A1));
					}

			result.setAt(i, at);

		}
		a.set(result);

		a.value = N_Operator.resolveTo(a.value, at);
		return a;
	}

	public static Number sum(aVector... v) {
		aVector V = new aVector();
		for (aVector a : v)
			V.add(a.cpy());
		return V.sum();
	}
}
