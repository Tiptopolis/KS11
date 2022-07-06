package com.uchump.prime.Core.Primitive.A_I;

import java.util.function.Supplier;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.Struct._Array;
import com.uchump.prime.Core.Primitive.Struct.aList;

public interface iNode<T> extends Comparable, Supplier<T> {

	// aMap<String,iLambda> Data

	public T get();

	public void set(T to);

	public default iNode<T> node() {
		return this;
	}

	public default <L> void label(L l) {

	}

	public default <L> L label() {
		return (L) (this.getClass().getSimpleName());
	}

	public default String hashId() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode());
	}

	public static _Array<iNode> Of(Object... T) {
		_Array<iNode> res = new _Array<iNode>();
		for (Object o : T) {
			if (o instanceof iNode)
				res.append((iNode) o);
			else
				res.append(new aNode(T));
		}
		return res;
	}
	
	public static<X> _Array<X> To(iNode<X>...N)
	{
		_Array<X> res = new _Array<X>();
		for (iNode<X> n : N) {
			res.append(n.get());
		}
		return res;
	}
}
