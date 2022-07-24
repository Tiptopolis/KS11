package com.uchump.prime.Core.System._ECS;

import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Metatron.Lib.dhETL.Model;

public class _Entity extends aValue<Model>{

	
	//public aMap<String,Object>
	
	public _Entity(String name) {
		super(name);
		this.value = new Model();
	}

}
