package com.uchump.prime.Core.Primitive;

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
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aSetMap;

public class aGroup<N extends Number, T> extends aNode<iCollection<T>> implements iGroup<N, iNode<T>> {
	// defacto NameSpace, basis of type
	//sibling nodes link to each other @ phase0 of this group & @ -1 to group itself
	public Supplier<Boolean> isTrivial = () -> {
		return this.size() == 1;
	};

	public aSetMap<N, iNode<T>> members;
	public Function<Predicate[], aGroup> Subgroup = (Predicate[] p) -> {
		return null;
	};

	public aGroup() {
		super();
	}

	public aGroup(aGroup G) {
		super();
		this.members = (aSetMap<N, iNode<T>>) G.members.cpy();
	}

	public aGroup(aGroup G, iCollection<Predicate> P) {

	}



	@Override
	public N firstIndex() {

		Number n = N_Operator.minOf(this.members.keys);
		return (N) n;
	}

	@Override
	public Iterator<iNode<T>> iterator() {
		return this.members.values.iterator();
	}
	
	@Override
	public aSetMap<N,iNode<T>> toMap()
	{
		return this.members;
		
	}

	@Override
	public iGroup join(iGroup other) {
		
		this.members.put(other.toMap().getEntries());
		return this;
	}

	@Override
	public void insert(N at, iNode<T> member) {
		// TODO Auto-generated method stub
		
	}


	
	@Override
	public iGroup with(iNode<T>... members) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public iGroup with(iGroup other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public iNode<T> get(N index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(N i, iNode<T> o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public N indexOf(Object member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(N at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(iNode<T> entry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public iGroup resize(int to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public iNode<T>[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	


}
