package com.uchump.prime.Core.Data.Primitive;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.Struct._Array;

public class aContainer<T> extends aNode<_Array<T>> implements iCollection<T>{

	
	public aContainer(iCollection<T> t)
	{
		super(new _Array<T>(t));		
	}
	//variable return type

	
	
	@Override
	public void insert(Integer at, T member) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T get(Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(Integer i, T o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer indexOf(Object member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Integer at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(T entry) {
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
	public Integer getIndexType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <N, X> iMap<N, X> toMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void append(T entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendAll(T... entries) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAt(int at, T to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swap(int i, int j) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T[] getComponentData() {
		// TODO Auto-generated method stub
		return null;
	}

}
