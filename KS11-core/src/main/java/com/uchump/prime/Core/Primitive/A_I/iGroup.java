package com.uchump.prime.Core.Primitive.A_I;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Primitive.Struct._Array;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aLinkedList;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public interface iGroup<IndexType extends Number, T> extends Iterable<T>, iContain<T> {


	// __________i_s_______________________
	// InnerPos @2#10->2 @%#
	// OutterPos @12#10->2 @%#
	// InnerNeg @-2#10->8 abs(#+@)%#
	// OutterNeg @-12#10->8 abs(#+@)%#
	public default IndexType resolveIndex(Number n) {
		IndexType IndexT = (IndexType) N_Operator.resolveTo(n, this.firstIndex());
		IndexType S = (IndexType) N_Operator.resolveTo(this.size(), IndexT);
		IndexType Z = (IndexType) N_Operator.resolveTo(0, IndexT);

		if (N_Operator.isEqual(IndexT, Z))
			return Z;

		float s = this.size();
		float i = n.floatValue();

		if (i < 0) {
			if (i + s > 0)
				i = N_Operator.abs(N_Operator.add(S, IndexT)).floatValue();
			// i = Math.abs(s + i);
			else
				i = N_Operator.sub(S, N_Operator.abs(N_Operator.add(S, IndexT))).floatValue();
			// i = s - Math.abs(s + i);
		}

		if (i > s) {
			i = (i % s);
		}

		return (IndexType) N_Operator.resolveTo(i, IndexT);
		// return this.firstIndex();
	}

	public IndexType firstIndex();

	public iGroup join(iGroup other);

	public void insert(IndexType at, T member);

	public default iGroup with(IndexType at, T member) {
		this.insert(at, member);
		return this;
	}

	public iGroup with(T... members);

	public iGroup with(iGroup other);

	public T get(IndexType index);

	public default T getOrDefault(IndexType key, T defaultValue) {
		T v;
		return ((v = get(key)) != null) ? v : defaultValue;
	}

	public void set(IndexType i, T o);

	public IndexType indexOf(Object member);

	public void remove(IndexType at);

	public default void remove(T e) {
		this.remove(this.indexOf(e));
	}

	public default T take(IndexType index) {
		T get = this.get(index);
		this.remove(index);
		return get;
	}

	public boolean contains(T entry);

	public boolean isEmpty();

	public int size();

	public iGroup resize(int to);

	public void clear();

	public IndexType getIndexType();

	public T[] toArray();

	public default iCollection<T> collectAll() {
		return new _Array<T>(this.toArray());
	}

	public <N, X> iMap<N, X> toMap();

	default aMap<IndexType, T> asMap() {
		aMap<IndexType, T> M = new aMap<IndexType, T>();

		for (int i = 0; i < this.size(); i++) {
			IndexType I = this.resolveIndex(i);
			M.put(I, this.get(I));
		}

		return M;
	}

	default void forEach(BiConsumer<? super IndexType, ? super T> action) {
		Objects.requireNonNull(action);
		for (_Map.Entry<IndexType, T> entry : this.asMap()) {
			IndexType k;
			T v;
			try {
				k = entry.getKey();
				v = entry.getValue();
			} catch (IllegalStateException ise) {
				// this usually means the entry is no longer in the map.
				continue;
			}
			action.accept(k, v);
		}
	}
	
	public default iGroup fromProperties(Properties props)
	{
		return this;
	}
   


}
