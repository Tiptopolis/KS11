package com.uchump.prime.Core.Data.Primitive;

import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;

public class aSchema extends aContainer<aField> {
	//FORM->SCHEMA->ENTITY
	//ComparatorMap
	//aSet<aField<?>> Fields = new aSet<aField<?>>(); //singleton fields
	
	public aSchema(iCollection<aField> t) {
		super(t);
	}

	
	
	
	public<X extends Object> void setField(String fieldName, int index, X val)
	{
	}
	
}
