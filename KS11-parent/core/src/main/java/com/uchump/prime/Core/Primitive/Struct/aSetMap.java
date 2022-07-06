package com.uchump.prime.Core.Primitive.Struct;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Iterator;

import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;

public class aSetMap<K, V> extends _Map<K, V> {

	// index-mapped lists
	// 1-1 mapping only

	@Override
	public void put(K key, Object val) {

		if (!this.containsKey(key)) {
			this.keys.append(key);
			this.values.append((V) val);
		} else {
			this.set(key, (V) val);
		}

	}

	public void put(K key, Object... vals) {
		for (Object v : vals) {
			this.put(key, v);
		}
	}

	@Override
	public void set(K key, V val) {
		// setAll
		if (!this.containsKey(key))
			this.put(key, val);
		int i = this.keys.indexOf(key);
		this.values.setAt(i, val);

	}
	
	@Override
	public  iMap<K, V> toMap() {
		return this;
	}

}
