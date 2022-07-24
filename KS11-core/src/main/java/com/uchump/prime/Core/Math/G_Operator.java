package com.uchump.prime.Core.Math;

import com.uchump.prime.Core.Primitive.iFunctor;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.Struct._Array;
import com.uchump.prime.Core.Primitive.Struct.aLinkedList;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public class G_Operator {
	
	public static <G extends iGroup, T> G newOf(Class<G> type)
	{
		switch(""+type.getSimpleName())
		{
		 case "_Array" -> new _Array<T>();
		 case "aSet" -> new aSet<T>();
		 case "aList" -> new aList<T>();
		 case "aLinkedList" -> new aLinkedList<T>();
		 case "aMap" -> new aMap();
		 case "aMultiMap" -> new aMultiMap();
		
		};
		return null;
	}
	
	public static aMap<String, iFunctor> Operators;

	public static iGroup add(iGroup a, iGroup b) {
		return a.with(b);
	}

	public static iGroup copyAdd(iGroup a, iGroup b, Class<? extends iGroup> type) {
		iGroup G = newOf(type);
		iCollection C = G.collectAll();
		
		C.with(a);
		C.with(b);
		return C;
	}

}
