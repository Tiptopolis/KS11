package com.uchump.prime.Core.Primitive;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.Struct._Array;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class aGroup<N extends Number, T> extends aNode<iCollection<T>> implements iGroup<N, T> {
	

	// defacto NameSpace, basis of type
	// sibling nodes link to each other @ phase0 of this group & @ -1 to group
	// itself
	public Supplier<Boolean> isTrivial = () -> {
		return this.size() == 1;
	};

	public aMap<N, T> members;
	public Function<Predicate[], aGroup> Subgroup = (Predicate[] p) -> {
		return null;
	};

	public aGroup() {
		super();
	}

	public aGroup(aGroup G) {
		super();
		this.members = (aMap<N, T>) G.members.cpy();
	}

	public aGroup(aGroup G, Predicate... P) {
		super();
		this.members = (aMap<N, T>) (G.members.cpy().filterValue(P));
	}

	@Override
	public N getIndexType() {
		return (N) N_Operator.resolveTo(1, this.firstIndex());
	}

	@Override
	public N firstIndex() {
		if (this.members.keys == null || this.members.keys.isEmpty())
			return (N) ((Number) 0);
		Number n = N_Operator.minOf(this.members.keys);
		return (N) n;
	}

	public N lastIndex() {
		if (this.members.keys == null || this.members.keys.isEmpty())
			return (N) ((Number) 0);

		Number n = N_Operator.maxOf(this.members.keys);
		Log("!!! " + n);
		return (N) n;
	}

	@Override
	public Iterator<T> iterator() {
		return this.members.values.iterator();
	}

	@Override
	public aMap<N, T> toMap() {
		return this.members;

	}

	@Override
	public iGroup join(iGroup other) {

		this.members.put(other.toMap().getEntries());
		return this;
	}

	@Override
	public void insert(N at, T member) {
		// TODO Auto-generated method stub

	}

	@Override
	public iGroup with(T... members) {
		if (this.members == null)
			this.members = new aMap<N, T>();
		for (T t : members) {
			N l = this.lastIndex();
			this.members.put(l, t);
		}

		return this;
	}

	@Override
	public iGroup with(iGroup other) {

		if (other.isEmpty())
			return this;

		if (this.members == null)
			this.members = new aMap<N, T>();

		this.members.with(other);

		return this;
	}

	@Override
	public T get(N index) {
		return this.members.get(index);
	}

	@Override
	public void set(N i, T o) {
		this.members.set(i, o);

	}

	@Override
	public N indexOf(Object member) {
		return (N) this.members.values.indexOf(member);
	}

	@Override
	public void remove(N at) {
		this.members.removeKey(at);

	}

	@Override
	public boolean contains(T entry) {
		return this.members.containsValue(entry);
	}

	@Override
	public boolean isEmpty() {
		if (this.members == null)
			return true;
		return this.members.isEmpty();
	}

	@Override
	public int size() {
		return this.members.size();
	}

	@Override
	public iGroup resize(int to) {
		return this.members.resize(to);
	}

	@Override
	public void clear() {
		this.members.clear();
	}

	@Override
	public T[] toArray() {
		return this.members.values.toArray();
	}

	@Override
	public iCollection<T> collectAll() {
		return this.members.values;
	}

	public String toToken() {
		String tag = "";
		tag = this.getClass().getSimpleName();
		return "<" + tag + ">";
	}
	
}
