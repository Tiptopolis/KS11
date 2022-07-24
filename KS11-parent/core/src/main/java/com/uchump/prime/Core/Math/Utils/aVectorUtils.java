package com.uchump.prime.Core.Math.Utils;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Comparator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;


public class aVectorUtils {

	static public final float FLOAT_ROUNDING_ERROR = 0.000001f;

	public static aVector index(int length) {
		aVector nV = new aVector();
		for (int i = 0; i < length; i++)
			nV.append(i);

		return nV;
	}

	public static aVector addressIndex(iCollection... toMap) {
		aVector n = new aVector();

		for (int i = 0; i < toMap.length; i++)
			n.append(toMap[i].size());

		return n;
	}

	public static <T> aMultiMap<aVector<Integer>, T> map(iCollection<T>... toMap) {
		aVector<Integer> dim = addressIndex(toMap);
		aVector<Integer>[] Dim = fillPermutations(dim);

		aMultiMap<aVector<Integer>, T> res = new aMultiMap<aVector<Integer>, T>();

		for (int i = 0; i < dim.size(); i++) {
			for (int j = 0; j < toMap[i].size(); j++) {
				T at = toMap[i].get(j);
				res.put(new aVector(i, j), at);
			}
		}

		return res;
	}

	public static <T> aMultiMap<aVector<Integer>, T> Map(aVector offset, iCollection<T>... toMap) {

		aMultiMap<aVector<Integer>, T> res = new aMultiMap<aVector<Integer>, T>();

		return res;
	}

	public static <T> T elementAt(aVector<Integer> address, iCollection<T>... toMap) {
		aMultiMap<aVector<Integer>, T> map = map(toMap);
		if (map.containsKey(address))
			return map.get(address);
		else
			return null;
	}

	// splits a vector into target size ~freq
	// so: 4,(1,2,3,4,5,6,7,8,9,10,11,12) splits at every 4th
	// becomes: (1,2,3,4)(5,6,7,8)(9,10,11,12)
	public static aVector[] splitEvery(int freqMod, aVector values) {
		int s = (values.size() / freqMod) + (values.size() % freqMod);
		// Log(" :: " + s);
		aVector[] nV = new aVector[s];

		aVector v = values.cpy();
		for (int i = 0; i < s; i++) {
			aVector[] V = v.split(freqMod);
			aVector n = V[0];
			v = V[1];
			nV[i] = n;
		}
		for (int i = 0; i < nV.length; i++)
			if (nV[i].size() < freqMod)
				nV[i].resize(freqMod);

		return nV;
	}

	public static aVector getLonger(aVector a, aVector b) {
		if (a.size() >= b.size())
			return a;
		else
			return b;
	}

	public static aVector getShorter(aVector a, aVector b) {
		if (a.size() < b.size())
			return a;
		else
			return b;
	}

	public static int longestSize(iCollection<aVector> vects) {
		int s = 0;
		for (aVector v : vects)
			if (v.size() > s)
				s = v.size();
		return s;
	}

	public static int longestSize(aVector... vects) {
		int s = 0;
		for (aVector v : vects)
			if (v.size() > s)
				s = v.size();
		return s;
	}

	public static int shortestSize(iCollection<aVector> vects) {
		int s = Integer.MAX_VALUE;
		for (aVector v : vects)
			if (v.size() < s)
				s = v.size();
		return s;
	}

	public static int shortestSize(aVector... vects) {
		int s = Integer.MAX_VALUE;

		for (aVector v : vects)
			if (v.size() < s)
				s = v.size();

		return s;
	}

	public static Number largestValue(aVector vect) {
		Number N = 0;
		for (int i = 0; i < vect.size(); i++) {
			if (aMaths.compareTo(vect.get(i), N) == 1)
				N = vect.get(i);
		}

		return N;
	}

	public static aVector max(aVector... vects) {
		int s = longestSize(vects);
		Number[] V = new Number[s];
		for (int i = 0; i < V.length; i++) {
			Number N = -Integer.MAX_VALUE;
			for (int j = 0; j < vects.length; j++) {

				N = aMaths.max(N, largestValue(vects[j]));
			}

			V[i] = N;
		}
		aVector res = new aVector(V);
		return res;
	}

	public static aVector min(aVector... vects) {
		int s = longestSize(vects);
		Number[] V = new Number[s];
		for (int i = 0; i < V.length; i++) {
			Number N = -Integer.MAX_VALUE;
			for (int j = 0; j < vects.length; j++) {

				N = aMaths.max(N, smallestValue(vects[j]));
			}

			V[i] = N;
		}
		aVector res = new aVector(V);
		return res;
	}

	public static Number smallestValue(aVector vect) {
		Number N = Integer.MAX_VALUE;
		for (int i = 0; i < vect.size(); i++) {
			if (aMaths.compareTo(vect.get(i), N) == -1)
				N = vect.get(i);
		}

		return N;
	}

	public static aVector dir(aVector a, aVector b) {
		return a.cpy().sub(b.cpy()).scl(-1);
	}

	public static aVector gradiant(aVector a, aVector b)
	{
		return aVectorUtils.dir(a.cpy(), b.cpy()).nor();
	}
	
	public static boolean isEqual(aVector a, aVector b) {
		return isEqual(a, b, FLOAT_ROUNDING_ERROR);
	}

	public static boolean isEqual(aVector a, aVector b, float epsilon) {
		return a.isEqual(b, epsilon);
	}
	
	public static boolean isEqual(Vector3 a, Vector3 b, float epsilon) {
		if (MathUtils.isEqual(a.x, b.x, epsilon) && MathUtils.isEqual(a.y, b.y, epsilon)
				&& MathUtils.isEqual(a.z, b.z, epsilon))
			return true;
		else
			return false;
	}
	


	public static aVector[] fillPermutations(aVector dimensions) {
		return listCoords(dimensions);
	}

	public aVector[] fillPermutations(Number... dimensions) {
		return listCoords(new aVector(dimensions));
	}

	// all coordinate pairs in area defined by vector, absolute val
	public static aVector[] listCoords(aVector dimensions) {
		aVector cumulatives = new aVector(dimensions.value).resize(dimensions.size());
		int total = 1;
		for (int d = dimensions.size() - 1; d >= 0; d--) {
			cumulatives.setAt(d, total);
			total *= dimensions.get(d).intValue();
		}

		aVector[] coords = new aVector[total];
		for (int i = 0; i < total; i++) {
			aVector coord = new aVector(dimensions.value).resize(dimensions.size());
			for (int d = dimensions.size() - 1; d >= 0; d--) {
				coord.setAt(d, (int) Math.floor(i / cumulatives.get(d).intValue()) % dimensions.get(d).intValue());
			}
			coords[i] = coord;
		}
		return coords;
	}

	public static aVector subDiv(aVector s, int to) {
		return subDiv(s, 0, to);
	}

	public static aVector subDiv(aVector s, int from, int to) {
		aVector out = new aVector();
		for (int i = from; i < to; i++)
			out.append(s.get(i));
		return out;
	}

	public static aVector doSubDiv(aVector s, int to) {
		return doSubDiv(s,0,to);
	}
	public static aVector doSubDiv(aVector s, int from, int to) {
		aVector out = new aVector();
		for (int i = from; i < to; i++)
			out.append(s.get(i));

		s.clear();
		s.set(out);

		return s;
	}


	public static Comparator<aVector> magnitudeComparator() {
		Comparator<aVector> m = new Comparator<aVector>() {

			@Override
			public int compare(aVector o1, aVector o2) {

				if (o1.elements.size() > o2.elements.size())
					return 1;
				if (o1.elements.size() < o2.elements.size())
					return -1;
				return 0;
			}

		};
		return m;
	}

	public static Comparator<aVector> sumComparator() {
		Comparator<aVector> m = new Comparator<aVector>() {

			@Override
			public int compare(aVector o1, aVector o2) {

				if (o1.sum().floatValue() > o2.sum().floatValue())
					return 1;
				if (o1.sum().floatValue() < o2.sum().floatValue())
					return -1;
				return 0;
			}

		};
		return m;
	}

	public static Comparator<aVector> elemComparator() {
		Comparator<aVector> m = new Comparator<aVector>() {

			@Override
			public int compare(aVector o1, aVector o2) {

				int weight = 0;
				// if (o1.elements.size() > o2.elements.size())
				// weight++;
				// if (o1.elements.size() < o2.elements.size())
				// weight--;

				Number a1 = largestValue(o1);
				Number a2 = largestValue(o2);

				if (a1.floatValue() < a2.floatValue())
					weight++;
				if (a1.floatValue() > a2.floatValue())
					weight--;

				if (o1.sum().floatValue() < o2.sum().floatValue())
					weight++;
				if (o1.sum().floatValue() > o2.sum().floatValue())
					weight--;
				if (o1.lMag() > o2.lMag())
					weight++;
				if (o1.lMag() < o2.lMag())
					weight--;
				return weight;
			}

		};
		return m;
	}

	public static iCollection<aVector> sortMagnitudeAscending(iCollection<aVector> vects) {
		return vects.sort(magnitudeComparator());
	}

	public static iCollection<aVector> sortMagnitudeDescending(iCollection<aVector> vects) {
		return vects.sort(magnitudeComparator()).reverse();
	}

	public static iCollection<aVector> sortSumAscending(iCollection<aVector> vects) {
		return vects.sort(sumComparator());
	}

	public static iCollection<aVector> sortSumDescending(iCollection<aVector> vects) {
		return vects.sort(sumComparator()).reverse();
	}

	public static iCollection<aVector> sortElementsAscending(iCollection<aVector> vects) {
		return vects.sort(elemComparator());
	}

	public static iCollection<aVector> sortElementsDescending(iCollection<aVector> vects) {
		return vects.sort(elemComparator()).reverse();
	}

	//////////////////////////////

	public static aVector inverseLerp(Number min, Number max, aVector value) {
		for (int i = 0; i < value.size(); i++) {
			value.setAt(i, aMaths.inverseLerp(min, max, value.get(i)));
		}
		return value;
	}

	public aVector remap(aValue.Range oldRange, aValue.Range newRange, aVector value) {

		int size = Math.min(2, value.size());
		for (int i = 0; i < size; i++) {
			Number N = N_Operator.resolveTo(value.get(i),0);
		}
		return value;
	}

	public static aVector dimOf(aVector... vects) {
		int s = longestSize(vects);
		aVector res = new aVector().filled(s, vects[0].value);

		res = max(vects);

		aVector offset = new aVector().filled(s, N_Operator.resolveTo(1, res.value));
		res.add(offset);
		return res;
	}

}
