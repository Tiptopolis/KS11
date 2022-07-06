package com.uchump.prime.Core.Primitive.Struct;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Utils.StringUtils;

public class aMap<K, V> extends _Map<K,V> {

	// index-mapped lists
	// 1-many[unique] mapping
	//public aList<K> keys;
	//public aList<V> values;

	public aMap() {
		this.keys = new aList<K>();
		this.values = new aList<V>();
	}

	@Override
	public  iMap<K, V> toMap() {
		return this;
	}








}
