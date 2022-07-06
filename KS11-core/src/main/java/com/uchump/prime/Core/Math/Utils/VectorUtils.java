package com.uchump.prime.Core.Math.Utils;

import static com.uchump.prime.Core.uAppUtils.*;
import static com.uchump.prime.Core.uSketcher.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import squidpony.squidmath.RNG;

public class VectorUtils {

	// *Undocumented*
	public static final float kEpsilon = 0.00001F;
	// *Undocumented*
	public static final float kEpsilonNormalSqrt = 1e-15F;


	public static final Vector3 zero = new Vector3();
	public static final Vector3 one = new Vector3(1, 1, 1);

	public static final Vector3 up = new Vector3(0, 0, 1);
	public static final Vector3 down = new Vector3(0, 0, -1);
	public static final Vector3 left = new Vector3(-1, 0, 0);
	public static final Vector3 right = new Vector3(1, 0, 0);
	public static final Vector3 forward = new Vector3(0, 1, 0);
	public static final Vector3 back = new Vector3(0, -1, 0);
	public static final Vector3 positiveInf = new Vector3().add(Float.POSITIVE_INFINITY);
	public static final Vector3 negativeInf = new Vector3().add(Float.NEGATIVE_INFINITY);

	public static RNG RndG = new RNG();

	public static long decomposeVector2(Vector2 v) {
		// least significant 16-bits; ie 16-bit coords
		return ((long) v.x & 0xFFFF) << 16 | ((long) v.y & 0xFFFF);
	}

	////
	//
	public static Vector3 unitTheta(Vector3 from, Vector3 to) {
		Vector3 ln = dir(from, to);
		float aX = (float) Math.atan2(ln.x, ln.y);
		float aY = (float) Math.atan2(ln.y, ln.z);
		float aZ = (float) Math.atan2(ln.z, ln.x);
		Vector3 out = new Vector3(aX, aY, aZ);

		return out;
	}

	public static Vector3 dirFromRotation(Matrix4 rot) {
		Quaternion q = rot.getRotation(new Quaternion(), true);
		Vector3 z = mul4x3(q, new Vector3(0, -1, 0));
		Vector3 correctedR = new Vector3(z.x, z.z, z.y);
		return correctedR;
	}

	public static Vector3 dirFromRotation(Quaternion rot) {
		Vector3 z = mul4x3(rot, new Vector3(0, -1, 0));
		Vector3 correctedR = new Vector3(z.x, z.z, z.y);
		return correctedR;
	}

	public static Vector3 randomDir() {
		// float u = (float) RNG.rndDouble(0, 360) * MathUtils.degRad;
		// float v = (float) RNG.rndDouble(0, 360) * MathUtils.degRad;
		float u = (float) RndG.nextFloat(360) * MathUtils.degRad;
		float v = (float) RndG.nextFloat(360) * MathUtils.degRad;
		Vector3 dir = new Vector3();

		float theta = MathUtils.PI2 * u; // azimuthal angle
		float phi = (float) Math.acos(2f * v - 1f); // polar angle

		return dir.setFromSpherical(theta, phi);
	}

	//
	// VECTOR SHIT
	//

	public static Vector2 toXZ(Vector3 v) {
		return new Vector2(v.x, v.z);
	}

	public static float average(Vector2 v) {
		return (v.x + v.y) / 2;
	}

	public static float average(Vector3 v) {
		return (v.x + v.y + v.z) / 3;
	}

	public static float average(Quaternion v) {
		return (v.x + v.y + v.z + v.w) / 4;
	}

	public static Vector3 abs(Vector3 v) {
		return new Vector3(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z));
	}

	////

	public static Vector2 div_Vector2(Vector2 a, float u) {
		return new Vector2(a.x / u, a.y / u);
	}

	public static Vector2 div_Vector2(Vector2 a, Vector2 b) {
		return new Vector2(a.x / b.x, a.y / b.y);
	}

	public static Vector3 div_Vector3(Vector3 a, float u) {
		return new Vector3(a.x / u, a.y / u, a.z / u);
	}

	public static Vector3 div_Vector3(Vector3 a, Vector3 b) {
		return new Vector3(a.x / b.x, a.y / b.y, a.z / b.z);
	}

	public static Vector2 div(Vector2 a, Vector2 b) {
		return div_Vector2(a, b);
	}

	public static Vector2 div(Vector2 a, Vector3 b) {
		return div_Vector2(a, downcast(b));
	}

	public static Vector3 div(Vector3 a, Vector3 b) {
		return div_Vector3(a, b);
	}

	public static Vector3 div(Vector3 a, Vector2 b) {
		return div_Vector3(a, upcast(b));
	}

	public static Vector3 div(Vector3 a, float b) {
		return div_Vector3(a, b);
	}

	////

	public static Vector2 dir(Vector2 a, Vector2 b) {
		return a.cpy().sub(b.cpy()).nor();
	}

	// "Direction" of a vector, can be used for moving shit like a compass; 0-1
	// (-1to1,-1to1)
	// float
	// a - at, b - from
	public static Vector3 dir(Vector3 a, Vector3 b) {
		return a.cpy().sub(b.cpy()).nor().scl(-1);
	}

	public static Vector3 dst(Vector3 a, Vector3 b) {
		return a.cpy().sub(b.cpy()).scl(-1);
	}

	public static Vector3 inc(Vector3 a, Vector3 b, int factor) {

		Vector3 dst = VectorUtils.dst(a.cpy(), b.cpy());
		Vector3 dv = new Vector3(factor, factor, factor);
		Vector3 inc = VectorUtils.div(dst, dv);

		return inc;
	}

	public static Vector2 IntVector2() {
		return new Vector2(0, 0);
	}

	public static Vector2 IntVector2(Vector2 vector) {
		return new Vector2((int) vector.x, (int) vector.y);
	}

	public static Vector2 IntVector2(float x, float y) {
		return IntVector2(new Vector2(x, y));
	}

	public static Vector2 IntVector2(int x, int y) {
		return IntVector2(new Vector2(x, y));
	}

	public static Vector2 IntVector2(Number x, Number y) {
		return IntVector2(new Vector2(x.floatValue(), y.floatValue()));
	}

	public static Vector3 euler(Vector3 direction) {
		Vector3 Euler = new Vector3((float) (Math.atan2(direction.x, direction.y)),
				(float) (Math.atan2(direction.y, direction.z)), (float) (Math.atan2(direction.z, direction.x)));
		return Euler;
	}

	public static Vector3 eulerAngles(Vector3 direction) {
		Vector3 Euler = new Vector3((float) (Math.atan2(direction.x, direction.y)),
				(float) (Math.atan2(direction.y, direction.z)), (float) (Math.atan2(direction.z, direction.x)));
		return Euler.scl(MathUtils.radDeg);
	}

	public static Vector3 pry(Quaternion q) {
		return new Vector3(q.getPitch(), q.getRoll(), q.getYaw());
	}

	public static void lookAt(Vector3 target, Vector3 it) {
		// Vector3 direction = dir(new Vector3(), it);
		Vector3 tmp = dir(target, it);

		// normalizeUp();

	}

	public static Vector3 rotateAround(Vector3 position, Vector3 point, Vector3 axis, float angle) {
		Vector3 tmpVec = new Vector3();
		tmpVec.set(point);
		tmpVec.sub(position.cpy());
		translate(position, tmpVec);
		rotate(position, axis, angle);
		tmpVec.rotate(axis, angle);
		translate(position, -tmpVec.x, -tmpVec.y, -tmpVec.z);
		return position;
	}

	public static void translate(Vector3 position, float x, float y, float z) {
		position.add(x, y, z);
	}

	public static void translate(Vector3 position, Vector3 vec) {
		position.add(vec);
	}

	public static void rotate(Vector3 position, Vector3 axis, float angle) {
		position.rotate(axis, angle);
		// up.rotate(axis, angle);
	}

	// regular line distance formula
	public static float calcDistance(Vector2 a, Vector2 b) {
		// sqrt((X2-X1)^2+(Y2-Y1)^2

		float result = 0;
		if (a.equals(b) || a == b) {
			return 0f;
		}

		result = a.dst(b);

		return result;
	}

	public static float calcDistance(Vector3 a, Vector3 b) {
		// sqrt((X2-X1)^2+(Y2-Y1)^2

		float result = 0;
		if (a.equals(b) || a == b) {
			return 0f;
		}

		result = a.dst(b);

		return result;
	}

	/////
	// ORBIT

	public static Vector3 calculateOrbit(float currentOrbitDegrees, float distanceFromCenterPoint,
			Vector3 centerPoint) {
		float radians = (float) Math.toRadians((currentOrbitDegrees * -1) + 90);

		float x = (float) ((Math.cos(radians) * distanceFromCenterPoint) + centerPoint.x);
		float y = (float) ((Math.sin(radians) * distanceFromCenterPoint) + centerPoint.y);
		float z = (float) ((Math.tan(radians) * distanceFromCenterPoint) + centerPoint.z);

		return new Vector3(x, y, z);
	}

	public static float mag(Vector3 v) {
		return v.len();
	}

	//////
	// MODULUS

	// NOTE: Vector Modulus typically refers to its length/magnitude

	public static Vector2 mod(Vector2 v, float mod) {

		return new Vector2(v.x % mod, v.y % mod);
	}

	public static float dotMod(Vector2 v, float mod) {
		Vector2 m = mod(v, mod);

		return m.x * v.x + m.y * v.y;
	}

	public static float crsMod(Vector2 v, float mod) {
		Vector2 m = mod(v, mod);

		return m.x * v.x - m.y * v.y;
	}

	public static Vector3 mod(Vector3 v, float mod) {
		return new Vector3(v.x % mod, v.y % mod, v.z % mod);
	}

	public static float dotMod(Vector3 v, float mod) {
		Vector3 m = mod(v, mod);

		return m.x * v.x + m.y * v.y + m.z * v.z;
	}

	public static float crsMod(Vector3 v, float mod) {
		Vector3 m = mod(v, mod);

		return m.x * v.x - m.y * v.y - m.z * v.z;
	}

	/////
	// CLAMPS
	public static Vector3 IntVector3() {
		return new Vector3(0, 0, 0);
	}

	public static Vector3 IntVector3(Vector2 vector) {
		return new Vector3((int) vector.x, (int) vector.y, 0);
	}

	public static Vector3 IntVector3(Vector3 vector) {
		return new Vector3((int) vector.x, (int) vector.y, (int) vector.z);
	}

	public static Vector3 IntVector3(Vector3 vector, boolean round) {
		if (round)
			return new Vector3((int) (vector.x + 0.5f), (int) (vector.y + 0.5f), (int) (vector.z + 0.5f));
		else
			return new Vector3((int) vector.x, (int) vector.y, (int) vector.z);
	}

	public static Vector3 IntVector3(float x, float y, float z) {
		return IntVector3(new Vector3(x, y, z));
	}

	public static Vector3 IntVector3(int x, int y, int z) {
		return IntVector3(new Vector3(x, y, z));
	}

	public static Vector3 IntVector3(Number x, Number y, Number z) {
		return IntVector3(new Vector3(x.floatValue(), y.floatValue(), z.floatValue()));
	}

	public static Vector3 IntVector3(Number x, Number y, Number z, boolean round) {

		return IntVector3(new Vector3(x.floatValue(), y.floatValue(), z.floatValue()));
	}

	public static Vector2 roundVect2(Vector2 vect, int roundTo) {
		BigDecimal vX = Maths.round(vect.x, roundTo);
		BigDecimal vY = Maths.round(vect.y, roundTo);

		return vect.set(vX.floatValue(), vY.floatValue());

	}

	public static Vector3 roundVect3(Vector3 vect, int roundTo) {
		BigDecimal vX = Maths.round(vect.x, roundTo);
		BigDecimal vY = Maths.round(vect.y, roundTo);
		BigDecimal vZ = Maths.round(vect.z, roundTo);

		return vect.set(vX.floatValue(), vY.floatValue(), vZ.floatValue());

	}

	public static Vector2 clampVect2(Vector2 vect, Vector2 range) {
		float x = MathUtils.clamp(vect.x, range.x, range.y);
		float y = MathUtils.clamp(vect.y, range.x, range.y);

		return vect.set(x, y);
	}

	public static Vector3 clampVect3(Vector3 vect, Vector2 range) {
		float x = MathUtils.clamp(vect.x, range.x, range.y);
		float y = MathUtils.clamp(vect.y, range.x, range.y);
		float z = MathUtils.clamp(vect.z, range.x, range.y);

		return vect.set(x, y, z);
	}

	// returns 'from' clamped from 0-1 as related with 'to'
	public Vector2 clampToVect2(Vector2 from, Vector2 max) {
		float iX = from.x / max.x;
		float iY = from.y / max.y;

		return from.set(iX, iY);

	}

	//////
	// CAST

	public static Vector3 upcast(Vector2 vect) {
		return new Vector3(vect.x, vect.y, 0);
	}

	public static Vector3 upcast(Vector2 vect, float z) {
		return new Vector3(vect.x, vect.y, z);
	}

	public static Vector2 downcast(Vector3 vect) {
		return new Vector2(vect.x, vect.y);
	}

	public static Vector2 downcast(Vector3 vect, int i) {

		int index = i % 3;
		switch (i) {
		case 0:
			return new Vector2(vect.x, vect.y);
		case 1:
			return new Vector2(vect.y, vect.z);
		case 2:
			return new Vector2(vect.z, vect.x);
		}

		return new Vector2(vect.x, vect.y);
	}

	public static Quaternion upcast(Vector3 vect) {
		return new Quaternion(vect.x, vect.y, vect.z, 0);
	}

	public static Quaternion upcast(Vector3 vect, float w) {
		return new Quaternion(vect.x, vect.y, vect.z, w);
	}

	public static Vector3 downcast(Quaternion q) {
		return new Vector3(q.x, q.y, q.z);
	}

	public static Vector3 downcast(Quaternion q, int i) {
		int index = i % 4;
		switch (i) {
		case 0:
			return new Vector3(q.x, q.y, q.z);
		case 1:
			return new Vector3(q.y, q.z, q.w);
		case 2:
			return new Vector3(q.z, q.w, q.x);
		case 3:
			return new Vector3(q.w, q.x, q.y);
		}

		return new Vector3(q.x, q.y, q.z);
	}

	//////
	// MANUVRE

	public static Vector2 uVector2(Number x, Number y) {
		return new Vector2(x.floatValue(), y.floatValue());
	}

	public static Vector3 uVector3(Number x, Number y, Number z) {
		return new Vector3(x.floatValue(), y.floatValue(), z.floatValue());
	}

	public static Quaternion uVector4(Number x, Number y, Number z, Number w) {
		return new Quaternion(x.floatValue(), y.floatValue(), z.floatValue(), w.floatValue());
	}

	public static Quaternion uQuaternion(Number x, Number y, Number z, Number w) {
		return new Quaternion(x.floatValue(), y.floatValue(), z.floatValue(), w.floatValue());
	}

	public float[] toFloatArray(Vector2 v) {
		float[] result = new float[2];
		result[0] = v.x;
		result[1] = v.y;

		return result;
	}

	public float[] toFloatArray(Vector3 v) {
		float[] result = new float[3];
		result[0] = v.x;
		result[1] = v.y;
		result[2] = v.y;

		return result;
	}

	public float[] toFloatArray(Quaternion v) {
		float[] result = new float[4];
		result[0] = v.x;
		result[1] = v.y;
		result[2] = v.y;
		result[3] = v.z;

		return result;
	}

	//////
	// ASSEMBLE

	// PRY
	public static Quaternion fromEuler(Vector3 angles) {
		return new Quaternion().setEulerAngles(angles.x, angles.y, angles.z);
	}

	public static Quaternion fromEulerRad(Vector3 angles) {
		return new Quaternion().setEulerAnglesRad(angles.x, angles.y, angles.z);
	}

	public static ArrayList<Vector2> assembleVect2(float[] vertices) {
		return assembleVect2(vertices, false);
	}

	public static ArrayList<Vector2> assembleVect2(float[] vertices, boolean dropTrailing) {
		ArrayList<Vector2> result = new ArrayList<Vector2>();

		int dT = 0;
		if (dropTrailing)
			dT = 1;

		for (int i = 0; i < vertices.length - dT; i += 2) {
			float nextX = 0;
			float nextY = 0;

			nextX = vertices[i];

			if (i >= vertices.length - 1) {
				nextY = 0;
			}

			else {
				nextY = vertices[i + dT];
			}
			result.add(new Vector2(nextX, nextY));
		}

		return result;
	}

	public static ArrayList<Vector3> assembleVect3(float[] vertices) {
		return assembleVect3(vertices, false);
	}

	public static ArrayList<Vector3> assembleVect3(float[] vertices, boolean dropTrailing) {
		ArrayList<Vector3> result = new ArrayList<Vector3>();

		int dT = 0;
		if (dropTrailing)
			dT = 2;

		for (int i = 0; i < vertices.length - dT; i += 3) {
			float nextX = 0;
			float nextY = 0;
			float nextZ = 0;

			nextX = vertices[i];
			nextY = vertices[i + 1];

			if (i >= vertices.length - 2) {
				nextZ = 0;
			}

			else {
				nextZ = vertices[i + 2];
			}
			result.add(new Vector3(nextX, nextY, nextZ));
		}

		return result;
	}

	public static ArrayList<Quaternion> assembleVect4(float[] vertices) {
		return assembleVect4(vertices, false);
	}

	public static ArrayList<Quaternion> assembleVect4(float[] vertices, boolean dropTrailing) {
		ArrayList<Quaternion> result = new ArrayList<Quaternion>();

		int dT = 0;
		if (dropTrailing)
			dT = 3;

		for (int i = 0; i < vertices.length - dT; i += 4) {
			float nextX = 0;
			float nextY = 0;
			float nextZ = 0;
			float nextW = 0;

			nextX = vertices[i];
			nextY = vertices[i + 1];
			nextZ = vertices[i + 2];

			if (i >= vertices.length - 3) {
				nextW = 0;
			}

			else {
				nextW = vertices[i + 3];
			}
			result.add(new Quaternion(nextX, nextY, nextZ, nextW));
		}

		return result;
	}

	//////////////////////////////////
	// converts to a stream of digits
	public static float[] disassembleVectors(Object... vectors) {
		return disassembleVects(vectors);
	}

	public static float[] disassembleVects(Object... vectors) {
		float[] result = new float[0];

		Object t = vectors[0];
		if (t instanceof Vector2) {
			result = new float[vectors.length * 2];
			int i = 0;
			Vector2[] vex2 = new Vector2[vectors.length];
			for (Object n : vectors) {
				Vector2 v = (Vector2) n;
				vex2[i] = v;
				i++;
			}
			return disassembleVect2(vex2);
		}

		if (t instanceof Vector3) {
			result = new float[vectors.length * 3];
			int i = 0;
			Vector3[] vex3 = new Vector3[vectors.length];
			for (Object n : vectors) {
				Vector3 v = (Vector3) n;
				vex3[i] = v;
				i++;
			}
			return disassembleVect3(vex3);
		}

		if (t instanceof Quaternion) {
			result = new float[vectors.length * 4];
			int i = 0;
			Quaternion[] vex4 = new Quaternion[vectors.length];
			for (Object n : vectors) {
				Quaternion v = (Quaternion) n;
				vex4[i] = v;
				i++;
			}
			return disassembleVect4(vex4);
		}

		if (t instanceof Color) {
			result = new float[vectors.length * 4];
			int i = 0;
			Quaternion[] vexc = new Quaternion[vectors.length];
			for (Object n : vectors) {
				Color c = (Color) n;
				Quaternion v = new Quaternion(c.r, c.g, c.b, c.a);
				vexc[i] = v;
				i++;
			}
			return disassembleVect4(vexc);
		}

		return result;
	}

	public static float[] disassembleVect2(Vector2... vectors) {
		float[] result = new float[vectors.length * 2];

		int i = 0;
		for (Vector2 v : vectors) {
			result[i] = v.x;
			result[i + 1] = v.y;
			i += 2;
		}

		return result;
	}

	public static float[] disassembleVect2(Object... objs) {
		Object o = objs[0];

		if (o instanceof Vector2) {
			float[] f = new float[objs.length * 2];
			for (int i = 0; i < objs.length; i++) {
				Object O = objs[i];
				Vector2 v = (Vector2) O;
				f[i] = v.x;
				f[i + 1] = v.y;
				return f;
			}

		}

		if (o instanceof Vector3) {
			float[] f = new float[objs.length * 2];
			for (int i = 0; i < objs.length; i++) {
				Object O = objs[i];
				Vector3 v = (Vector3) O;
				f[i] = v.x;
				f[i + 1] = v.y;
			}
			return f;

		}
		return null;
	}

	public static float[] disassembleVect3(Vector3... vectors) {
		float[] result = new float[vectors.length * 3];

		int i = 0;
		for (Vector3 v : vectors) {
			result[i] = v.x;
			result[i + 1] = v.y;
			result[i + 2] = v.z;
			i += 3;
		}

		return result;
	}

	public static float[] disassembleVect4(Quaternion... vectors) {
		float[] result = new float[vectors.length * 4];

		int i = 0;
		for (Quaternion v : vectors) {
			result[i] = v.x;
			result[i + 1] = v.y;
			result[i + 2] = v.z;
			result[i + 3] = v.w;
			i += 4;
		}

		return result;
	}

	////

	public static Vector3 rndVect3() {
		return new Vector3((float) Math.random(), (float) Math.random(), (float) Math.random());
	}

	// quick rotation?

	// Vector3 v = this.getCameraDirection();
	// Quaternion q = new Quaternion().setEulerAngles(0, 0, 0);
	// v.rot(new Matrix4(q));
	public static Vector3 mul4x3(Quaternion rotation, Vector3 point) {
		float x = rotation.x * 2F;
		float y = rotation.y * 2F;
		float z = rotation.z * 2F;
		float xx = rotation.x * x;
		float yy = rotation.y * y;
		float zz = rotation.z * z;
		float xy = rotation.x * y;
		float xz = rotation.x * z;
		float yz = rotation.y * z;
		float wx = rotation.w * x;
		float wy = rotation.w * y;
		float wz = rotation.w * z;

		Vector3 res = new Vector3();
		res.x = (1F - (yy + zz)) * point.x + (xy - wz) * point.y + (xz + wy) * point.z;
		res.y = (xy + wz) * point.x + (1F - (xx + zz)) * point.y + (yz - wx) * point.z;
		res.z = (xz - wy) * point.x + (yz + wx) * point.y + (1F - (xx + yy)) * point.z;
		return res;
	}

	// You can just multiply this matrix by your camera matrix and it will give you
	// what you want. org.lwjgl.util.Quaternion has the method
	// Quaternion.setFromAxisAngle() for constructing a quaternion from a vector and
	// an orientation. My guess is that you should be using that.
	public static Matrix4 rotationFromQuaternion(Quaternion q) {
		Matrix4 mat = new Matrix4();

		float xSq = q.x * q.x;
		float ySq = q.y * q.y;
		float zSq = q.z * q.z;
		float wSq = q.w * q.w;
		float twoX = 2.0f * q.x;
		float twoY = 2.0f * q.y;
		float twoW = 2.0f * q.w;
		float xy = twoX * q.y;
		float xz = twoX * q.z;
		float yz = twoY * q.z;
		float wx = twoW * q.x;
		float wy = twoW * q.y;
		float wz = twoW * q.z;

		// mat.m00 = wSq + xSq - ySq - zSq;
		// mat.m10 = xy - wz;
		// mat.m20 = xz + wy;
		// mat.m30 = 0;
		// mat.m01 = xy + wz;
		// mat.m11 = wSq - xSq + ySq - zSq;
		// mat.m21 = yz - wx;
		// mat.m31 = 0;
		// mat.m02 = xz - wy;
		// mat.m12 = yz + wx;
		// mat.m22 = wSq - xSq - ySq + zSq;
		// mat.m32 = 0;
		// mat.m03 = 0;
		// mat.m13 = 0;
		// mat.m23 = 0;
		// mat.m33 = 1;
		// return mat;

		float m00 = wSq + xSq - ySq - zSq;
		float m10 = xy - wz;
		float m20 = xz + wy;
		float m30 = 0;
		float m01 = xy + wz;
		float m11 = wSq - xSq + ySq - zSq;
		float m21 = yz - wx;
		float m31 = 0;
		float m02 = xz - wy;
		float m12 = yz + wx;
		float m22 = wSq - xSq - ySq + zSq;
		float m32 = 0;
		float m03 = 0;
		float m13 = 0;
		float m23 = 0;
		float m33 = 1;

		Vector3 xAxis = new Vector3(m00, m10, m20);
		Vector3 yAxis = new Vector3(m01, m11, m21);
		Vector3 zAxis = new Vector3(m02, m12, m22);
		Vector3 pos = new Vector3();
		// return new Matrix4().set(xAxis, yAxis,zAxis, pos);
		return new Matrix4().set(xAxis, yAxis, zAxis, pos);
	}

	/*
	 * public static aVector getShorter(aVector a, aVector b) { if (a.elements.size
	 * > b.elements.size) return a; else return b; }
	 * 
	 * public static aVector getLonger(aVector a, aVector b) { if (b.elements.size >
	 * a.elements.size) return b; else return a; }
	 */

	public static Vector3 map(Vector3 val, Vector3 oMin, Vector3 oMax, Vector3 nMin, Vector3 nMax) {
		Vector3 result;
		// return ( (val - oMin) / (oMax - oMin) ) * (nMax - nMin) + nMin;
		// a-> (v.cpy().sub(oMin.cpy())
		// b-> (o.cpy().sub(oMin.cpy())

		Vector3 deltaF = val.cpy().sub(oMin.cpy());
		Vector3 deltaO = oMax.cpy().sub(oMin.cpy());
		Vector3 deltaN = nMax.cpy().sub(nMin.cpy());
		// (deltaF/DeltaO)*(deltaN)+nMin

		Vector3 divFO = div(deltaF, deltaO);
		Vector3 mulFON = divFO.scl(deltaN);

		result = mulFON.add(nMin.cpy());
		return result;
	}

	// demo
	public static Vector3 unproject(Vector3 screenCoords, float viewportX, float viewportY, float viewportWidth,
			float viewportHeight) {
		float x = screenCoords.x, y = screenCoords.y;
		x = x - viewportX;
		y = Gdx.graphics.getHeight() - y;
		y = y - viewportY;
		screenCoords.x = (2 * x) / viewportWidth - 1;
		screenCoords.y = (2 * y) / viewportHeight - 1;
		screenCoords.z = 2 * screenCoords.z - 1;

		Matrix4 ProjectionView = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Matrix4 invProjectionView = ProjectionView.cpy().inv();
		screenCoords.prj(invProjectionView);
		return screenCoords;
	}

	/*
	 * public static Vector3 unproject(Vector3 screenCoords, Rect viewport) { //
	 * only works at 1:1 unit factor, cant zoom out yet Vector3 vPos = new
	 * Vector3(viewport.minX, viewport.minY, 0); float x = screenCoords.x, y =
	 * screenCoords.y; x = x - viewport.minX; y = Gdx.graphics.getHeight() - y; y =
	 * y - viewport.minY; screenCoords.x = (2 * x) / viewport.width - 1;
	 * screenCoords.y = (2 * y) / viewport.height - 1; screenCoords.z = 2 *
	 * screenCoords.z - 1;
	 * 
	 * Matrix4 ProjectionView = new Matrix4().setToOrtho2D(0, 0,
	 * Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); Matrix4 invProjectionView
	 * = ProjectionView.cpy().inv(); screenCoords.prj(invProjectionView);
	 * 
	 * Vector3 pointMod = screenCoords.cpy(); pointMod.x = (pointMod.x + ((vPos.x *
	 * 2) - (Gdx.graphics.getWidth() / 2))); pointMod.y = (pointMod.y + ((vPos.y *
	 * 2) - (Gdx.graphics.getHeight() / 2))); return pointMod; }
	 */

	public static Vector3 project(Vector3 worldCoords, float viewportX, float viewportY, float viewportWidth,
			float viewportHeight) {
		// Matrix4 combined = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight());
		Matrix4 combined = new Matrix4().setToProjection(0, Gdx.graphics.getWidth(), 0, Gdx.graphics.getHeight(), 0, 1);
		worldCoords.prj(combined);
		worldCoords.x = viewportWidth * (worldCoords.x + 1) / 2 + viewportX;
		worldCoords.y = viewportHeight * (worldCoords.y + 1) / 2 + viewportY;
		worldCoords.z = (worldCoords.z + 1) / 2;
		return worldCoords;
	}

	/*
	 * public static Vector3 unproject(Vector3 point, Rect projector, Matrix4
	 * projection) { Vector3 vSize = new Vector3(); Vector3 vPosition = new
	 * Vector3(0, 0, 0); // Log(projector.toLog()); float x = point.x; float y =
	 * point.y; vPosition.set(projector.minX, projector.minY, 0);
	 * vSize.set(projector.width, projector.height, 0); x = x - vPosition.x;
	 * 
	 * y = Height - y; // screen.height y = y - vPosition.y;
	 * 
	 * point.x = (2 * x) / Width - 1; point.y = (2 * y) / Height - 1; point.z = (2 *
	 * point.z - 1);
	 * 
	 * point.prj(projection.inv()); // Log("" + point);
	 * 
	 * Vector3 pointMod = point.cpy(); pointMod.x = pointMod.x + vPosition.x + 1;
	 * pointMod.y = pointMod.y + vPosition.y + 1; return pointMod; }
	 */

	/*
	 * public static Vector3 unproject(Vector3 point, Rect projector, Rect
	 * projection) { Vector3 vSize = new Vector3(); Vector3 vPosition = new
	 * Vector3(0, 0, 0); // Log(projector.toLog()); float x = point.x; float y =
	 * point.y; vPosition.set(projector.minX, projector.minY, 0);
	 * vSize.set(projector.width, projector.height, 0); x = x - vPosition.x;
	 * 
	 * y = Height - y; // screen.height y = y - vPosition.y;
	 * 
	 * point.x = (2 * x) / Width - 1; point.y = (2 * y) / Height - 1; point.z = (2 *
	 * point.z - 1);
	 * 
	 * point.prj(projection.getMatrix().inv()); // Log("" + point);
	 * 
	 * Vector3 pointMod = point.cpy(); pointMod.x = pointMod.x + vPosition.x + 1;
	 * pointMod.y = pointMod.y + vPosition.y + 1; return pointMod; }
	 */

	/*
	 * public static Vector3 unproject(Vector3 point, Rect projector, Matrix4
	 * projection) { Vector3 vSize = new Vector3(); Vector3 vPosition = new
	 * Vector3(0, 0, 0); // Log(projector.toLog()); float x = point.x; float y =
	 * point.y; vPosition.set(projector.minX, projector.minY, 0);
	 * vSize.set(projector.width, projector.height, 0); x = x - vPosition.x;
	 * 
	 * y = Height - y; // screen.height y = y - vPosition.y;
	 * 
	 * point.x = (2 * x) / Width - 1; point.y = (2 * y) / Height - 1; point.z = (2 *
	 * point.z - 1);
	 * 
	 * point.prj(projection.inv()); // Log("" + point);
	 * 
	 * Vector3 pointMod = point.cpy(); pointMod.x = pointMod.x + vPosition.x + 1;
	 * pointMod.y = pointMod.y + vPosition.y + 1; return pointMod; }
	 */

	public static boolean isEqual(Vector3 a, Vector3 b) {
		if (MathUtils.isEqual(a.x, b.x) && MathUtils.isEqual(a.y, b.y) && MathUtils.isEqual(a.z, b.z))
			return true;
		else
			return false;
	}

	public static boolean isEqual(Vector3 a, Vector3 b, float epsilon) {
		if (MathUtils.isEqual(a.x, b.x, epsilon) && MathUtils.isEqual(a.y, b.y, epsilon)
				&& MathUtils.isEqual(a.z, b.z, epsilon))
			return true;
		else
			return false;
	}





}
