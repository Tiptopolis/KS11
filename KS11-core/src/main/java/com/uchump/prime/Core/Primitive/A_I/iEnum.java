package com.uchump.prime.Core.Primitive.A_I;

import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Utils._SQL.Type;

public interface iEnum<T> extends iConstructor<T>, iCollection<T> {

	@Override
	public default void set(Integer i, T o) {

	}

	@Override
	public default void insert(Integer at, T member) {

	}

	@Override
	public default void clear() {

	}

	@Override
	public default void remove(Integer at) {

	}

	@Override
	public default void append(T entry) {

	}

	@Override
	public default void appendAll(T... entries) {

	}

	@Override
	public default void setAt(int at, T to) {

	}

	@Override
	public default void swap(int i, int j) {

	}

	@Override
	public default boolean isEmpty() {

		return false;
	}

	@Override
	public default boolean contains(T entry) {

		return true;
	}

	@Override
	public default iGroup resize(int to) {
		return this;
	}

	public default void dispose() {

	}

	public default aMap<Type, String> getTags() {
		return new aMap<Type, String>();
	}

	@Override
	public default T[] getComponentData() {

		return this.toArray();
	}

	public static abstract class _ALL {
		public static aMap<String, iEnum> ENUMS;

		public static void dispose() {
			if (ENUMS != null) {
				for (iEnum E : ENUMS.values)
					dispose(E);
				ENUMS.clear();
				ENUMS = null;
			}
		}

		public static void dispose(iEnum e) {

			e.getTags().clear();
			e.dispose();
		}
	}
}
