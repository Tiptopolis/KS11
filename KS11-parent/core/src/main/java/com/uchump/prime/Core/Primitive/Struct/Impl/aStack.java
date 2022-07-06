package com.uchump.prime.Core.Primitive.Struct.Impl;

import com.uchump.prime.Core.Primitive.A_I.iSequence;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public class aStack<T> extends aSet<T> implements iSequence<T>{
	
	// new entries, append to top
	// pull/pop from bottom(size()-1)
	//LIFO
	
	public void push(T obj)
	{
		this.append(obj);
	}
	
	public T pop()
	{
		return this.take(this.size()-1);
	}
	
	public T peek()
	{
		return this.get(this.size()-1);
	}
	
	@Override
	public int length() {
		return this.size();
	}

	@Override
	public T[] subSequence(int start, int end) {
		T[] out = (T[]) new Object[end-start];
		for(int i = 0,j=start; j < end; i++,j++)
			out[j]=this.data[i];
		
		return out;
	}
}
