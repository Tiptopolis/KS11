package com.uchump.prime.Core.Primitive.A_I;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Primitive.Struct.aSetMap;

public interface iGroup<IndexType extends Number, T> extends Iterable<T> {

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
				//i = Math.abs(s + i);
			else
				i = N_Operator.sub(S,N_Operator.abs(N_Operator.add(S, IndexT))).floatValue();
				//i = s - Math.abs(s + i);
		}

		if (i > s) {
			i = (i % s);
		}
		
		return (IndexType) N_Operator.resolveTo(i,IndexT);
		//return this.firstIndex();
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

	public T[] toArray();
	
	public<N,X> iMap<N,X> toMap();

}
