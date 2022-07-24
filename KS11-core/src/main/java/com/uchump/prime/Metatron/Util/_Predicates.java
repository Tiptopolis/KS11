package com.uchump.prime.Metatron.Util;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class _Predicates {
	
	
	
	//Supplier       ()    -> x
	//Consumer       x     -> ()
	//BiConsumer     x, y  -> ()
	//Callable       ()    -> x throws ex
	//Runnable       ()    -> ()
	//Function       x     -> y
	//BiFunction     x,y   -> z
	//Predicate      x     -> boolean
	//UnaryOperator  x1    -> x2
	//BinaryOperator x1,x2 -> x3

	public static interface Function<I, O> extends BiFunction<I, O, I> {

	}

	public static <T> Predicate<T> combineFiltersAll(Predicate<T>... predicates) {

		Predicate<T> p = Stream.of(predicates).reduce(x -> true, Predicate::and);
		return p;
	}

	public static <T> Predicate<T> combineFiltersOr(Predicate<T>... predicates) {

		Predicate<T> p = Stream.of(predicates).reduce(x -> true, Predicate::or);
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



	// public<T> static Predicate<T> build(Predicate p, T...values)
	// {

	// }

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

	// RESOLVER
	// isPredicates
	


}
