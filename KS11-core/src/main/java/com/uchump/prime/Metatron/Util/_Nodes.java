package com.uchump.prime.Metatron.Util;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Metatron.Lib.exp4M.tokenizer._Token;

public class _Nodes {

	public static <T> aNode<T> nodeOf(T obj) {
		return new aNode<T>(obj);
	}

	public static <T> aNode<Class<T>> nodeOfClass(Class<T> obj) {
		return new aNode<Class<T>>(obj);
	}

	public static <T> aNode<Class<T>> nodeOfClass(T obj) {
		return nodeOfClass((Class<T>) obj.getClass());
	}

	public static <T, C extends iCollection<T>> aNode<C> nodeOfGroup(C obj) {
		return new aNode<C>(obj);
	}

	public static <T, N extends Number, G extends iGroup<N, T>> aNode<G> nodeOfGroup(G obj) {
		return new aNode<G>(obj);
	}
	
	public static <T extends _Token> aNode<T> tokenNode(T token)
	{
		return new aNode<T>(token);
	}

}
