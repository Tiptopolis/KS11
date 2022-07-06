package com.uchump.prime.Core.Primitive.Struct.Impl;

import com.uchump.prime.Core.Primitive.A_I.iSequence;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public class aQueue<T> extends aSet<T> implements iSequence<T>{
	// new entries, append to top
	// pull/pop from bottom(0)
	//FIFO
	
	public void push(T obj)
	{
		this.append(obj);
	}
	
	public T pop()
	{
		return this.take(0);
	}
	
	public T peek()
	{
		return this.get(0);
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
