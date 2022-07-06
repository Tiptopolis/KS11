package com.uchump.prime.Core.Utils;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;

public class _Nodes {

	public static <T> aNode<T> nodeOf(T obj) {
		return new aNode<T>(obj);
	}

	public static <T, C extends iCollection<T>> aNode<C> nodeOfGroup(C obj) {
		return new aNode<C>(obj);
	}
	
	public static <T, N extends Number, G extends iGroup<N,T>> aNode<G> nodeOfGroup(G obj) {
		return new aNode<G>(obj);
	}

}
