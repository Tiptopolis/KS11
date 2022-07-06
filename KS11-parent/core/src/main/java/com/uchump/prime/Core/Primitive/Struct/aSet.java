package com.uchump.prime.Core.Primitive.Struct;

import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;

public class aSet<T> extends _Array<T> {

	public aSet() {
		super();
		this.data = (T[]) new Object[0];
	}

	public aSet(T... entries) {
		super();
		for (T t : entries)
			this.append(t);
	}

	public aSet(iGroup entries) {
		this();
		this.join(entries);
	}

	// add at end
	@Override
	public void append(T object) {

		if (!this.contains(object)) {
			this.modCount++;
			int i = data.length + 1;
			this.resize(i);
			data[i - 1] = object;
		}
	}

	// add all to end, sequentially
	public void append(T... objects) {
		for (int i = 0; i < objects.length; i++) {
			this.append(objects[i]);
		}
	}

	public void append(iCollection<T> C) {
		if (C instanceof aList) {
			for (int i = 0; i < C.size(); i++) {
				T t = C.get(i);
				this.append(t);
			}
		}
		if (C instanceof aSet) {
			for (T t : C) {
				this.append(t);
			}
		}
	}

	// add at index, shift others down
	@Override
	public void insert(Integer atIndex, T entry) {
		if (!this.contains(entry))
			super.insert(atIndex, entry);
		else
			this.swap(this.indexOf(entry), atIndex);
	}

}
