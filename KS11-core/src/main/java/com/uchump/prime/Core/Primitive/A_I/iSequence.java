package com.uchump.prime.Core.Primitive.A_I;

public interface iSequence<T> extends iCollection<T>{
	
	public int length();
	
	public T[] subSequence(int start, int end);
	
	public default void push(T obj) {
		this.append(obj);
	}

	public default T pop() {
		return this.take(this.size() - 1);
	}
	
	public default T poll()
	{
		return this.take(0);
	}

	public default T peek() {
		return this.get(this.size() - 1);
	}

	public default T top() {
		return this.get(this.size() - 1);
	}

	public default T end() {
		return this.get(0);
	}
}
