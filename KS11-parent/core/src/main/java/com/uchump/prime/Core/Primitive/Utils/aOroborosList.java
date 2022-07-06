package com.uchump.prime.Core.Primitive.Utils;

import com.uchump.prime.Core.Primitive.Struct.aList;

public class aOroborosList<T> extends aList<T> {

	public int max = 32;
	private int inTheta = 1; // insertion size, each step is appended # times
	private int outTheta = 1; // deletion size, each step removes # from head

	public aOroborosList() {
		super();
	}

	public aOroborosList(int max, int in, int out) {
		super();

		this.max = max;
		this.inTheta = in;
		this.outTheta = out;
	}

	public boolean isFull() {
		return this.size() % this.max == 0;
	}

	@Override
	public void append(T t) {
		this.step();
		for (int i = 0; i < inTheta; i++) {
			super.append(t);
		}
	}

	protected void step() {
		if (!this.isEmpty())
			if (this.isFull())
				for (int i = 0; i < outTheta; i++)
					this.remove(0);

	}
}
