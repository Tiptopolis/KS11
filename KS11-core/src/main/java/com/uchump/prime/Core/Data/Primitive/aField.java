package com.uchump.prime.Core.Data.Primitive;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.Struct._Array;
import com.uchump.prime.Core.Primitive.Struct._Map;

//column field header
public class aField<I,T> extends _Map.Entry<I,Class<T>>{

	//public String name; //speed
	public _Array<String> notes; //(km/h)
	
	public aField(I label, Class<T> type)
	{
		super(label,type);
	}

}
