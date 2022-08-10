package com.uchump.prime.Core.Primitive;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public interface iFunctor<T> {

	public default <O, I> O apply(I... args) {
		return this.get();
	}

	public default <S> S get() {
		return null;
	};

	public default void apply() {

	}

	public default <X> void accept(T args) {

	}

	public default <X> void accept(T... args) {

	}

	public default String fName() {
		return "Functor";
	}

	@FunctionalInterface
	public static interface Action extends iFunctor<Object> {
		public void execute();

		@Override
		public default void apply() {
			this.execute();
		}

		public default String fName() {
			return "Action";
		}
	}

	// meh
	@FunctionalInterface
	public static interface DoTo<T> extends iFunctor<Object> {
		public T execute(iCollection<T> ents, Function<Object, T> fn);

	}

	@FunctionalInterface
	public static interface Supplier<T> extends iFunctor<T>, java.util.function.Supplier<T> {
		@Override
		public T get();

		@Override
		public default T apply(Object... args) {
			return this.get();
		}

		public default String fName() {
			return "Supplier";
		}
	}

	@FunctionalInterface
	public static interface Consumer<T> extends iFunctor<T>, java.util.function.Consumer<T> {
		@Override
		void accept(Object t);

		public default String fName() {
			return "Consumer";
		}
	}

	@FunctionalInterface
	public static interface Effect<T> extends iFunctor<T>, java.util.function.Function<T, T> {
		@Override
		public T apply(T t);

		@Override
		public default T apply(Object... t) {
			for (int i = 0; i < t.length; i++)
				t[i] = this.apply((T) t[i]);

			return (T) t;
		}

		public default String fName() {
			return "Effect";
		}
	}

	@FunctionalInterface
	public static interface Function<I, O> extends iFunctor<O>, java.util.function.Function<I, O> {
		@Override
		public default O apply(I t) {
			return this.apply((Object) t, false);
		};

		@Override
		public O apply(Object... t);

		@Override
		public default void apply() {
			this.apply(this);
		}

		public default String fName()
		{
			return "Function";
		}
		
	}

	public static interface Operator<I, O> extends iFunctor<O>, java.util.function.BiFunction<I, I, O> {
		@Override
		public O apply(I t, I u);
		
		
		public default String fName()
		{
			return "Operator";
		}
	}

	// from SPARK
	@FunctionalInterface
	public static interface Route<Request, Response> extends iFunctor<Route<Request, Response>> {
		/**
		 * Invoked when a request is made on this route's corresponding path e.g.
		 * '/hello'
		 *
		 * @param request  The request object providing information about the HTTP
		 *                 request
		 * @param response The response object providing functionality for modifying the
		 *                 response
		 * @return The content to be set in the response
		 * @throws java.lang.Exception implementation can choose to throw exception
		 */
		Object handle(Request request, Response response) throws Exception;
	}

	@FunctionalInterface
	public interface RouteGroup {
		void addRoutes();
	}

	/*
	 * public static interface Case<T>{ //extends Function<Predicate<T>,T>{ //single
	 * predicate //predicate, output
	 * 
	 * // if(CASE[A].contains(o))
	 * 
	 * 
	 * 
	 * private default boolean contains(Object o) { return false; } }
	 */

	@FunctionalInterface
	public static interface Constructor<T> extends iFunctor<T> {

		public T getNew(iFunctor<T> fn);

		public default T getNew(aMap<String, Object> params, iFunctor<T> fn) {
			return null;
		}
	}

	public static class aExpression extends _Map.Entry<String, iFunctor> {
		public aExpression(String label, iFunctor f) {
			super(label, f);
		}

		public void evaluate() {
			this.value.apply();
		}

		public Object evaluate(Object... o) {
			return this.value.apply(o);
		}
	}
}
