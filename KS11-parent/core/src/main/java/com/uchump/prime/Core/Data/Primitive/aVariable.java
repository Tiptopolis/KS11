package com.uchump.prime.Core.Data.Primitive;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Metatron.Lib.exp4M.tokenizer.Tokens.Variable;

public class aVariable<T> extends aNode<Object>{

	//variable return type
	//if A return Object
	//if B return AssClown
	
	public aMap<String,T> Case = new aMap<String,T>();
	
	public aVariable(String label, T def)
	{
		super();
		this.label = label;
		this.value = (T) aVariable.class;//default case
		this.Case.put("def",def);
	}
}
