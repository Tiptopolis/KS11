package com.uchump.prime.Core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Primitive.iFunctor;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Utils.StringUtils;

import squidpony.squidmath.RNG;

public class uAppUtils {

	// public static uApp appInstance;
	public static uAppUtils instance;
	public static Vector2 DEFAULT_RESOLUTION = new Vector2(640, 480);
	public static Vector2 altRatio = new Vector2(1, 1);
	public static Matrix4 ScreenPrj;
	public static RNG RND = new RNG();

	static {
		uAppUtils.instance = new uAppUtils();
	}

	public static int Width = Gdx.graphics.getWidth();
	public static int Height = Gdx.graphics.getHeight();
	public static float AspectRatio;
	public static Matrix4 ScreenProjection;

	public static Vector2 MouseRaw = new Vector2(0, 0); // use this for camera projection
	public static Vector2 MouseScreenPos = new Vector2(0, 0); // move to a static UI handler?
	public static int MouseX = 0;
	public static int MouseY = 0;

	/*
	 * public uAppUtils(uApp app) { appInstance = app; instance = this; }
	 */

	public uAppUtils() {

		instance = this;
		ScreenProjection = new Matrix4().setToOrtho2D(0, 0, Width, Height);

	}

	public static void update(float deltaTime) {
		MouseRaw.set(Gdx.input.getX(), Gdx.input.getY());
		MouseScreenPos.set(Gdx.input.getX(), Height - Gdx.input.getY() - 1);
		MouseX = Gdx.input.getX();
		MouseY = Gdx.input.getY();

	}

	public static void resize() {
		Width = Gdx.graphics.getWidth();
		Height = Gdx.graphics.getHeight();
		ScreenPrj = new Matrix4().setToOrtho2D(0, 0, Width, Height);

		if (Height == 0)
			Height = 1;
		AspectRatio = Width / Height;
		ScreenProjection = new Matrix4().setToOrtho2D(0, 0, Width, Height);
		altRatio = new Vector2(Width / DEFAULT_RESOLUTION.x, Height / DEFAULT_RESOLUTION.y);
	}

	public static float mod(float a, float b) {
		return a % b;
	}

	public static Vector3 mod(Vector3 a, float b) {
		return new Vector3(a.x % b, a.y % b, a.z % b);
	}

	// ??
	public static Vector3 mod(float a, Vector3 b) {
		return new Vector3(a % b.x, a % b.y, a % b.z);
	}

	public static Vector3 mod(Vector3 a, Vector3 b) {
		return new Vector3(a.x % b.x, a.y % b.y, a.z % b.z);
	}

	public static String[] charArray(String s) {
		String[] res = new String[s.length()];

		for (int i = 0; i < s.length(); i++)
			res[i] = "" + s.charAt(i);

		return res;
	}

	public static void Log() {
		Log("");
	}

	public static void Log(aVector v) {

		System.out.println(v.toString());
	}

	public static void Log(String s) {

		System.out.println(s);
	}

	public static void Log(iFunctor f) {
		Log(f.fName());
	}

	public static void Log(Object s) {
		if (s != null)
			System.out.println(s.toString());
		else
			Log("null");
	}

	public static void Log(Object[] S) {

		for (int i = 0; i < S.length; i++) {
			if (S[i] == null) {
				Log("S[i] is null :?" + i);
				// if(S[i]!= null)
			} else
				Log(i + "/" + (S.length - 1) + " :  " + S[i].toString());
		}
	}

	public static void Log(Object[][] S) {

		Log(Arrays.deepToString(S));// split at "],"

	}

	public static void Log(float[] s) {
		for (int i = 0; i < s.length; i++) {
			if (s[i] == Float.NaN)
				Log("S[i] is null :?" + i);
			// if(S[i]!= null)
			Log(i + "/" + (s.length - 1) + " :  " + s[i]);
		}
	}

	public static void Log(iCollection c) {

		Log(c.toCSV());
	}

	public static void Log(Supplier s) {
		if (s != null && s.get() != null)
			Log(s.get());
		else
			Log("null");
	}

	public static void Log(iFunctor.Supplier s) {
		if (s != null && s.get() != null)
			Log(s.get());
		else
			Log("null");
	}

	public static String LogBack(Object o) {
		Log(o);
		return "" + o;
	}
	
	


	public static void Page() {
		String out = "";
		out += "\n";
		for (int i = 0; i < 180; i++) {
			out += ("_");
		}
		out += "\n";
		Log(out);
	}

	public static void print(String s) {
		Log(s);
	}

	// can be used as a basic object parser
	@SuppressWarnings("unused")
	static public void printArray(Object what) {
		System.out.println("PRINT ARRAY: " + what + " " + what.getClass());
		System.out.println("           : " + what.getClass().getName());
		System.out.println("           : " + what.getClass().getSimpleName());
		System.out.println("           : " + what.getClass().getCanonicalName());
		System.out.println("");

		if (what == null) {
			// special case since this does fuggly things on > 1.1
			System.out.println("null");

		} else {
			String name = what.getClass().getName();
			if (name.charAt(0) == '[') {
				switch (name.charAt(1)) {
				case '[':
					// don't even mess with multi-dimensional arrays (case '[')
					// or anything else that's not int, float, boolean, char
					System.out.println(what);
					break;

				case 'L':
					// print a 1D array of objects as individual elements
					Object poo[] = (Object[]) what;
					for (int i = 0; i < poo.length; i++) {
						if (poo[i] instanceof String) {
							System.out.println("[" + i + "] \"" + poo[i] + "\"" + " -|String|");
						} else {
							System.out.println("[" + i + "] " + poo[i]);
						}
					}
					break;

				case 'Z': // boolean
					boolean zz[] = (boolean[]) what;
					for (int i = 0; i < zz.length; i++) {
						System.out.println("[" + i + "] " + zz[i] + " -|boolean|");
					}
					break;

				case 'B': // byte
					byte bb[] = (byte[]) what;
					for (int i = 0; i < bb.length; i++) {
						System.out.println("[" + i + "] " + bb[i] + " -|byte|");
					}
					break;

				case 'C': // char
					char cc[] = (char[]) what;
					for (int i = 0; i < cc.length; i++) {
						System.out.println("[" + i + "] '" + cc[i] + "'" + " -|char|");
					}
					break;

				case 'I': // int
					int ii[] = (int[]) what;
					for (int i = 0; i < ii.length; i++) {
						System.out.println("[" + i + "] " + ii[i] + " -|int|");
					}
					break;

				case 'J': // lonn int
					long jj[] = (long[]) what;
					for (int i = 0; i < jj.length; i++) {
						System.out.println("[" + i + "] " + jj[i] + " -|long|");
					}
					break;

				case 'F': // float
					float ff[] = (float[]) what;
					for (int i = 0; i < ff.length; i++) {
						System.out.println("[" + i + "] " + ff[i] + " -|float|");
					}
					break;

				case 'D': // double
					double dd[] = (double[]) what;
					for (int i = 0; i < dd.length; i++) {
						System.out.println("[" + i + "] " + dd[i] + " -|double|");
					}
					break;

				default:
					System.out.println(what);
				}
			} else { // not an array
				System.out.println(what.toString());
			}
		}
		System.out.flush();
	}

	//
	// NUMBER STUFF
	//

	public static String isOddOrEven(Number n) {
		if ((n.byteValue() ^ 1) == n.byteValue() + 1) {
			return ("Even");
		} else
			return ("Odd");
	}

	// reduce vector to int -> (int nSeed = y << 16 | x;)
	////

	// CENTER->MOUSE CURSOR ROTATION in radians
	// Rotation = (float) Math.atan2((Height - MouseY) - (Height / 2), MouseX -
	// (Width / 2));
	////

	// DRAW A SPIRAL
	// float r = 0; // radius default increment +0.1
	// float theta = 0;// default increment +0.01
	// --updateFn
	// float x = (float) (r * Math.cos(theta));
	// float y = (float) (r * Math.sin(theta));
	// uSketcher.Drawer.setColor(COLOR);
	// uSketcher.Drawer.filledEllipse(x + center.x, y + center.y, 1, 1);
	////
	public static <T> boolean isInstanceOf(Class<T> clazz, Class<T> targetClass) {
		return clazz.isInstance(targetClass);
	}

	public boolean isArray(Object o) {
		return o.getClass().isArray();
	}

	//
	////
	// @Annotation Reflection hax
	//
	// Instead of AccessibleObject.getAnnotation(Class annotationClass) use
	private static Annotation getAnnotation(AccessibleObject object, Class annotationClass) {
		for (Annotation a : object.getAnnotations()) {
			if (a.annotationType().getCanonicalName().equals(annotationClass.getCanonicalName()))
				return a;
		}
		return null;
	}

	// Instead of MyAnnotation.value() use
	private static Object getAnnotationFieldWithReflection(Annotation annotation, String fieldName)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		return annotation.annotationType().getMethod(fieldName).invoke(annotation);
	}

	//////////////////////

	public static Number rnd(Number min, Number max) {
		if (max instanceof Integer)
			return rndInt(max.intValue(), max.intValue());

		else
			return rndFlt(max.floatValue(), max.floatValue());
	}

	public static int rndInt(int max) {
		return rndInt(0, max);
	}

	public static int rndInt(int min, int max) {
		return RND.between(min, max);
	}

	public static float rndFlt(float max) {
		return (float) RND.between(0f, max);
	}

	public static float rndFlt(float min, float max) {
		return (float) RND.between(min, max);
	}

	public static Object getRandom(Object... things) {
		RNG R = new RNG();
		int at = R.nextInt(things.length - 1);
		// int at = RNG.rndInt(0, things.length - 1);
		Log(" :: " + at);
		return things[at];
	}

	public static Object[] sample(Object[] things) {
		int size = things.length;

		ArrayList<Object> list = new ArrayList<Object>();
		int c = 0;
		while (c < size - 1) {
			Object next = getRandom(things);
			Log(list.size() + " <" + next + " " + next.getClass().getSimpleName());
			if (!list.contains(next)) {
				list.add(next);
				c++;
			}
			Log(list.get(0).equals(next));
		}

		return list.toArray();
	}

	public static ArrayList<Object> sampleCollection(Object[] things, int size) {
		if (size > things.length)
			size = things.length;
		ArrayList<Object> list = new ArrayList<Object>();
		int c = 0;
		while (c < size - 1) {
			Object next = getRandom(things);
			Log(list.size() + " <" + next + " " + next.getClass().getSimpleName());
			if (!list.contains(next)) {
				list.add(next);
				c++;
			}
			Log(list.get(0).equals(next));
		}

		return list;
	}

	public static String toHashId(Object obj) {
		return Integer.toHexString(obj.hashCode());
	}

	public static String toIdString(Object obj) {

		if (obj instanceof String || obj instanceof Number)
			return ("" + obj);
		else
			return obj.getClass().getSimpleName() + "@" + Integer.toHexString(obj.hashCode());
	}

	public static String toToken(Class obj) {
		String tag = "";
		tag = obj.getSimpleName();
		return "<" + tag + ">";
	}

	public static String toToken(Object obj) {
		String tag = "";
		tag = obj.getClass().getSimpleName();
		return "<" + tag + ">";
	}

	public static <T> Predicate<T> combineFilters(Predicate<T>... predicates) {

		Predicate<T> p = Stream.of(predicates).reduce(x -> true, Predicate::and);
		return p;

	}

	public static Predicate instanceOf(Class c) {
		return o -> c.isAssignableFrom(o.getClass());
	}

	public static Predicate instanceOf(Class... C) {
		int l = C.length;
		Predicate[] P = new Predicate[l];
		P[0] = instanceOf(C[0]);
		for (int i = 0; i < l; i++) {
			if (i != 0)
				P[i] = instanceOf(C[i]);
		}
		return any(P);
	}

	public static Predicate instanceOf(Object c) {
		return o -> o.getClass().isAssignableFrom(c.getClass());
	}

	public static Predicate instanceOf(Object... C) {
		int l = C.length;
		Predicate[] P = new Predicate[l];
		P[0] = instanceOf(C[0].getClass());
		for (int i = 0; i < l; i++) {
			if (i != 0)
				P[i] = instanceOf(C[i].getClass());
		}
		return any(P);
	}

	public static Predicate kindOf(Class c) {
		return o -> StringUtils.isFormOf(o.getClass().getSimpleName(), c.getSimpleName());
	}

	public static Predicate kindOf(Object c) {
		return kindOf(c.getClass());
	}

	public static Predicate kindOf(Class... C) {
		int l = C.length;
		Predicate[] P = new Predicate[l];
		P[0] = kindOf(C[0]);
		for (int i = 0; i < l; i++) {
			if (i != 0)
				P[i] = kindOf(C[i]);
		}
		return any(P);
	}

	public static Predicate kindOf(Object... C) {
		int l = C.length;
		Predicate[] P = new Predicate[l];
		P[0] = kindOf(C[0].getClass());
		for (int i = 0; i < l; i++) {
			if (i != 0)
				P[i] = kindOf(C[i].getClass());
		}
		return any(P);
	}

	public static boolean AllMatch(Predicate P, Object... O) {
		for (Object o : O)
			if (!P.test(o))
				return false;

		return true;
	}

	public static <T> Predicate<T> isValue(T v) {
		if (v instanceof Number)
			return o -> N_Operator.isEqual((Number) o, (Number) v);
		else
			return o -> o.equals(v);
	}

	public static <T> Predicate<T> isAnyValue(T... v) {
		int l = v.length;
		Predicate[] P = new Predicate[l];
		P[0] = isValue(v[0]);
		for (int i = 0; i < l; i++) {
			if (i != 0)
				P[i] = isValue(v[i]);
		}
		return any(P);
	}

	public static <T> Predicate<T> isReference(T v) {
		return o -> o == v;
	}

	public static <T> Predicate<T> isAnyReference(T... v) {
		int l = v.length;
		Predicate[] P = new Predicate[l];
		P[0] = isReference(v[0]);
		for (int i = 0; i < l; i++) {
			if (i != 0)
				P[i] = isReference(v[i]);
		}
		return any(P);
	}

	public static Predicate any(Predicate... p) {
		Predicate init = p[0];
		if (p.length > 1) {
			Predicate out = init;
			for (Predicate P : p)
				out = out.or(P);
			return out;
		} else
			return init;
	}

	public static Predicate all(Predicate... p) {
		Predicate init = p[0];
		if (p.length > 1) {
			Predicate out = init;
			for (Predicate P : p)
				out = out.and(P);
			return out;
		} else
			return init;
	}

	public static BiFunction equals() {
		return (a, b) -> {
			return a.equals(b);
		};
	}

	public static Function<Comparable, Integer> comparison(Comparable a) {
		return (o) -> (a.compareTo(o));
	}

	public static Comparator compareUsing(Function f) {
		return Comparator.comparing(f);
	}

	public static Comparator equality() {
		return compareUsing((Function) equals());
	}

	public static Predicate isEqual(Object other) {
		return (o) -> (o == other || o.equals(other));
	}
}
