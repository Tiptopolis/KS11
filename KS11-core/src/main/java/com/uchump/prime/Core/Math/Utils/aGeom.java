package com.uchump.prime.Core.Math.Utils;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.aTransform;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.Geom;
import com.uchump.prime.Core.Math.Utils.VectorUtils;
import com.uchump.prime.Core.Math.Utils.aVectorUtils;
import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Utils.StringUtils;

public abstract class aGeom {

	public static boolean contains(aVector[] verts, aVector point) {
		aVector pos = new aVector().filled(point.size(), N_Operator.resolveTo(0, point.get()));
		return contains(verts, pos, point);
	}

	public static boolean contains(aVector[] verts, aVector position, aVector point) {
		int base = verts[0].size();
		int cBase = point.size();

		aVector v = point.cpy();
		aVector pos = position.cpy();

		aList<aVector> pts = new aList<aVector>();
		for (int i = 0; i < verts.length; i++) {
			int n = i + 1;
			if (i == verts.length - 1) // mod? nah
				n = 0;

			aVector P = verts[i];
			aVector N = verts[n];

			// lineTo
			aVector M = midpoint(P, N);

			pts.append(P);
			pts.append(M);

		}

		for (aVector p : pts)
			if (aVector.dst(pos, v).len() > aVector.dst(pos, p).len())
				return false;

		return true;

	}

	public static boolean raySphereIntersects(aVector rayOrigin, aVector rayDir, aVector sphereOrigin, float radius) {
		float t = rayDir.cpy().dot(sphereOrigin.cpy().sub(rayOrigin.cpy()).len()).floatValue();// dst along rayDir
		aVector P = rayOrigin.cpy().add(rayDir.cpy().scl(t));
		float y = sphereOrigin.cpy().sub(P.cpy()).len();

		return y <= radius;
	}

	// private static void ___LINES___() {

	// }

	public static aVector[] interpolate(aVector a, aVector b) {

		aVector A = a.cpy().toType(0f);
		aVector B = b.cpy().toType(0f);
		aVector dst = A.cpy().dst(B.cpy());
		// int div = (int) ((dst.len() / 3) * 2) + 1;

		int div = (int) dst.len();

		return interpolate(a, b, div);
	}

	public static aVector[] interpolate(aVector a, aVector b, float freq) {
		return interpolate(a, b, ((int) (1f / freq)));
	}

	
	
	public static aVector[] interpolate(aVector a, aVector b, int div) {

		if (div < 1)
			div = 1;

		aList<aVector> r = new aList<aVector>();

		aVector A = a.cpy().toType(0f);
		aVector B = b.cpy().toType(0f);

		aVector dst = A.cpy().dst(B.cpy());
		aVector divisor = new aVector<Number>().filled(aVectorUtils.longestSize(a, b), (float) div);
		aVector step = dst.cpy().div((float) div);

		r.append(a);
		aVector next = A.cpy();
		for (int i = 0; i < div; i++) {
			aVector n = next.cpy().add(step.cpy());
			next = n;

			if (i != div - 1)
				r.append(next);
		}

		r.append(b);

		aVector[] R = new aVector[r.size()];
		for (int i = 0; i < r.size(); i++) {

			R[i] = r.get(i).toType(a.value).collapseRight();
			if (R[i].size() == 0)
				R[i].append(N_Operator.resolveTo(0, R[i].value));
		}

		aSet<aVector> Res = new aSet<aVector>(R);

		int maxSize = aVectorUtils.longestSize(r);//

		R = new aVector[Res.size()];
		for (int i = 0; i < Res.size(); i++) {

			R[i] = Res.get(i).toType(a.value).collapseRight();
			if (R[i].size() == 0)
				R[i].append(N_Operator.resolveTo(0, R[i].value));

			R[i].resize(maxSize);
		}
		return R;
	}

	public static aVector[] interpolate(Number a, Number b) {
		if (a instanceof aVector && b instanceof aVector)
			return interpolate((aVector) a, (aVector) b);
		else if (a instanceof aVector ^ b instanceof aVector) {
			aVector A = null;
			aVector B = null;
			if (a instanceof aVector)
				A = (aVector) a;
			else
				A = new aVector(a);
			if (b instanceof aVector)
				B = (aVector) b;
			else
				B = (aVector) b;
			return interpolate((aVector) A, (aVector) B);
		}
		return interpolate(new aVector(a), new aVector(b));
	}

	public static aVector[] interpolate(Number a, Number b, Number f) {
		if (a instanceof aVector && b instanceof aVector) {
			if (f instanceof Integer)
				return interpolate((aVector) a, (aVector) b, f.intValue());
			else
				return interpolate((aVector) a, (aVector) b, f.floatValue());

		} else if (a instanceof aVector ^ b instanceof aVector) {
			aVector A = null;
			aVector B = null;
			if (a instanceof aVector)
				A = (aVector) a;
			else
				A = new aVector(a);
			if (b instanceof aVector)
				B = (aVector) b;
			else
				B = (aVector) b;
			return interpolate(A, B, f);
		}
		return interpolate(new aVector(a), new aVector(b), f);
	}

	// LINE
	public static aVector[] lineTo(aVector from, aVector to) {
		return interpolate(from, to);
	}

	public static aVector[] lineTo(aVector from, aVector to, int div) {
		return interpolate(from, to, div);
	}

	public static aVector[] lineTo(aVector from, aVector to, float freq) {
		return interpolate(from, to, freq);
	}

	public static aVector centroid(aVector... vects) {
		aVector out = new aVector();

		for (aVector v : vects)
			out.add(v.cpy());

		out = out.div(vects.length);

		return out;
	}

	public static aVector midpoint(aVector a, aVector b) {
		aVector[] v = interpolate(a, b, 2);
		if (v.length >= 2)
			return v[1];
		else
			return v[0];

	}

	public static float calcIntersectAngle(aVector lineAfrom, aVector lineAto, aVector lineBfrom, aVector lineBto) {
		// line1(y1-y2,x1-x2)
		// line2(y1-y2,x1-x2)
		aVector lP1 = lineAfrom.cpy().sub(lineAto.cpy());
		aVector lP2 = lineBfrom.cpy().sub(lineBto.cpy());

		float a1 = (float) Math.atan2(lP1.get(1).floatValue(), lP1.get(0).floatValue());
		float a2 = (float) Math.atan2(lP2.get(1).floatValue(), lP2.get(0).floatValue());
		return a1 - a2;
	}

	// calculates perpendicular direction of line @ midpt between given points
	// where as dir is the 'direction' of the line, this is the 'up'
	public static aVector getLineNormalPositive(aVector from, aVector to) {
		int dim = Math.min(from.size(), to.size());
		aVector D = aVector.Axis(dim); // axis-default up

		aVector mid = midpoint(from, to);
		// aVector mid = getCentroid(from,to);
		// aVector mid = calcMidpoint(from, to);
		aVector dir = aVectorUtils.dir(from.cpy(), to.cpy());
		aVector rN = mid.sub(dir.crs(D).nor().scl(MathUtils.PI));// rotation formula
		return rN;
	}

	// left, local-posative
	public static aVector getNormalPositive(aVector... poly) {
		int dim = aVectorUtils.shortestSize(poly);

		aVector D = aVector.Axis(dim);

		aVector mid = centroid(poly);
		aVector dir = mid.cpy().sub(D).nor();

		aVector lP = mid.sub(dir.crs(D).nor()/* .scl(MathUtils.PI) */); // $in of vector?

		return lP;
	}

	public static aVector getNormalPositiveX(aVector... poly) {
		aVector dir = poly[0];
		aVector lP = centroid().cpy().add((dir.cpy().nor().scl(MathUtils.PI2)));
		return lP;
	}

	public static void rotateAround(aVector position, aVector point, aVector axis, float angle) {
		// position is vector being rotated
		//
		// rotateInner
		aVector dir = position.cpy().dir(point.cpy());
		dir.rotate(axis, angle);
		Log("__ " + dir);
		// rotateOutter
		aVector dst = position.cpy().dst(point.cpy());
		float len = dst.len();
		aVector rot = point.cpy().add(dir.scl(dst));
		position.set(point.add(rot));

	}

	// slope of a line
	public static aVector dir(aVector a, aVector b) {
		return a.cpy().sub(b.cpy()).scl(-1);
	}

	public static aVector[] integralVolume(Number n) {
		aVector dim = new aVector();

		if (!(n instanceof aVector)) {
			dim.fill(n.intValue(), n.intValue());
			return integralVolume(dim.toArray());
		} else {
			dim = (aVector) n;
			return integralVolume(dim.toArray());
		}
	}

	public static aVector[] integralVolume(Number... dimensionality) {

		aVector offset = new aVector().filled(dimensionality.length, 0f);
		return integralVolume(offset, dimensionality);
	}

	public static aVector[] integralVolume(aVector offset, Number... dimensionality) {
		aVector dim = new aVector(dimensionality);
		aVector[] Volume = aVectorUtils.fillPermutations(dim);

		for (aVector V : Volume) {
			V.sub(offset);
		}

		return Volume;
	}

	public static aTransform[] associateTransform(aTransform T, aVector[] vects) {
		aTransform[] res = new aTransform[vects.length];
		for (int i = 0; i < vects.length; i++) {
			aTransform newT = new aTransform(T, vects[i]);
			res[i] = newT;
			newT.setParent(T);
		}

		return res;
	}

	public static aTransform[] transformVolume(aTransform T, aVector offset, Number... dim) {
		return associateTransform(T, integralVolume(offset, dim));
	}

	public static aTransform[] transformVolume(aTransform T, String op, Number... dim) {
		aVector Dim = new aVector(dim);
		aVector offset = new aVector().filled(dim.length, 0f);

		if (StringUtils.isFormOf(op, "center"))
			offset = Dim.div(2f);
		return associateTransform(T, integralVolume(offset, dim));
	}

	public static aTransform[] transformVolume(aTransform T, Number... dim) {
		return associateTransform(T, integralVolume(dim));
	}

	public static float angle(aVector from, aVector to) {
		// sqrt(a) * sqrt(b) = sqrt(a * b) -- valid for real numbers
		float denom = from.len2() * to.len2();
		if (denom < MathUtils.FLOAT_ROUNDING_ERROR)
			return 0f;

		float dot = MathUtils.clamp(from.cpy().dot(to.cpy()).floatValue() / denom, -1, 1);
		return MathUtils.acos(dot) * MathUtils.radDeg;

	}

	public static float angle(aVector from, aVector to, aVector axis) {

		float unsigned = angle(from, to);

		// float crsX = from.y * to.z - from.z * to.y;
		// float crsY = from.z * to.x - from.x * to.z;
		// float crsZ = from.x * to.y - from.y * to.x;

		// float sign = Math.signum(axis.x * crsX + axis.y * crsY + axis.z * crsZ);

		aVector C = from.cpy().crs(to.cpy());
		aVector A = C.mul(axis.cpy());
		float sign = Math.signum(A.sum().floatValue());

		return unsigned * sign;

	}

	public static aVector slope(aVector a, aVector b) {

		return a.cpy().sub(b.cpy()).nor();

	}

	public static float zScale(aVector point, aVector reference, aVector unit) {
		float z = 1f;
		float unitLen = unit.len();

		z = (unitLen / reference.cpy().sub(point.cpy()).len());
		z = (z * (unit.len() / 3)) / 2;
		z = z * (unit.len() / 2);

		return z;
	}

	public static aVector[] distanceSort(aVector from, aVector... vects) {
		Arrays.sort(vects, distanceComparator(from));
		return vects;
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

	public static Comparator<aVector> distanceComparator(aVector point) {
		final aVector finalP = point.cpy();
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

	public static Comparator<aTransform> distanceComparator(aTransform point) {
		return distanceComparator(point, false);
	}

	public static Comparator<aTransform> distanceComparator(aTransform point, boolean local) {
		final aTransform finalP = point.cpy();
		aVector fp = new aVector(point.position(local));
		return new Comparator<aTransform>() {
			@Override
			public int compare(aTransform a, aTransform b) {

				aVector p0 = a.position(local);
				aVector p1 = b.position(local);
				if (p0 == null || p1 == null)
					return 0;

				double ds0 = p0.dstLen(fp);
				double ds1 = p1.dstLen(fp);

				if (aMaths.isEqual(ds0, ds1, MathUtils.FLOAT_ROUNDING_ERROR * 2f)) {
					ds1 += (MathUtils.FLOAT_ROUNDING_ERROR * 8f);
					ds0 -= (MathUtils.FLOAT_ROUNDING_ERROR);
				}

				return Double.compare(ds1, ds0);
			}

		};
	}

	public static aVector[] genPoly(aTransform t, int n) {
		aVector pos = t.getPosition();
		aVector dir = t.getRotation();
		aVector scl = t.getScale();
		aVector up = t.getNormal();

		aVector[] points = new aVector[n];
		float angle = MathUtils.PI2 / n;

		for (int i = 0; i < n; i++) {

			aVector Dir = dir.cpy().rotate(up, (angle * i) * MathUtils.radDeg);
			aVector pt = pos.cpy().add(Dir.cpy());

			points[i] = pt;
		}

		return points;
	}

	public static aVector[] genPoly(aTransform t, int n, int unit) {
		aVector pos = t.getLocalPosition();
		aVector dir = t.getLocalRotation();
		aVector scl = t.getLocalScale();
		aVector up = t.getLocalNormal();

		// Log(1/0);
		// return genPoly(pos, dir, scl, n);
		return genPoly(pos, dir, scl.cpy().mul(unit), n);
	}

	public static aVector[] genPoly(aVector pos, aVector dir, aVector scl, int n) {

		aVector[] points = new aVector[n];
		float angle = MathUtils.PI2 / n;

		for (int i = 0; i < n; i++) {

			aVector Dir = dir.cpy().rotate(new aVector(0, 0, 1), (angle * i) * MathUtils.radDeg);
			aVector pt = pos.cpy().add(Dir.cpy().scl(scl.cpy()));

			points[i] = pt;

		}

		return points;

	}

	public static aVector[] genPoly(aVector pos, aVector dir, aVector up, aVector scl, int n) {

		aVector[] points = new aVector[n];
		float angle = MathUtils.PI2 / n;

		for (int i = 0; i < n; i++) {

			aVector Dir = dir.cpy().rotate(up, (angle * i) * MathUtils.radDeg);
			aVector pt = pos.cpy().add(Dir.cpy().scl(scl.cpy()));

			points[i] = pt;
		}
		return points;
	}


	public static aTransform[] bindPoly(aTransform t, int n, float unit) {
		aVector pos = t.getLocalPosition();
		aVector dir = t.getRotation();
		aVector scl = t.getScale();
		aVector up = t.getNormal();

		// Log(1/0);
		aVector[] vals = genPoly(pos, dir, scl.mul(unit), n);
		aTransform[] res = new aTransform[vals.length];
		for (int i = 0; i < vals.length; i++) {
			res[i] = new aTransform();
			res[i].setParent(t);
			res[i].setPosition(vals[i]);
		}

		return res;
	}

	//////
	// CIRCLE ESTIMATOR
	static float PI7 = MathUtils.PI * 7;
	static int sideMultiplier = 1;
	static int minimumSides = 11;
	static int maximumSides = 21;

	public static int estimateSidesRequired(float pixelSize, float radiusX, float radiusY) {
		float circumference = (float) (MathUtils.PI2 * Math.sqrt((radiusX * radiusX + radiusY * radiusY) / 2f));
		int sides = (int) (circumference / (16 * pixelSize));
		float a = Math.min(radiusX, radiusY), b = Math.max(radiusX, radiusY);
		float eccentricity = (float) Math.sqrt(1 - ((a * a) / (b * b)));
		sides += (sides * eccentricity * sideMultiplier) / 16;
		return MathUtils.clamp(sides, minimumSides, maximumSides);
	}

}
