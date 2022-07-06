package com.uchump.prime.Core.Primitive.Struct;

import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;

public class aList<T> extends _Array<T> {
	
	public aList() {
		super();
		this.data = (T[]) new Object[0];
	}

	public aList(T... entries) {
		super(entries);
		
	}
	
	public aList(iGroup entries) {
		super(entries);		
	}
	
	@Override
	public void append(T entry) {
		super.append(entry);
	}

	@Override
	public void appendAll(T... entries) {
		for (int i = 0; i < entries.length; i++) {
			this.append(entries[i]);
		}
	}

}
