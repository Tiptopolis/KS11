package com.uchump.prime.Core.Primitive;

import com.uchump.prime.Core.Primitive.A_I.iToken;
import com.uchump.prime.Core.Primitive.Com.iContent;
import com.uchump.prime.Core.Primitive.Struct.aDictionary;
import com.uchump.prime.Metatron.Lib.exp4M.tokenizer._Token;

public class aToken<T> implements iToken<T>, iContent{

	public static aDictionary<String> TYPES = new aDictionary<String>();
	public T value;
	public Object type;
	
	static {
		if (TYPES == null)
			TYPES = new aDictionary<String>();

	}
	
	public static String getType(aToken T) {
		return "";
	}

	public String getName(aToken T) {
		return "";
	}



	@Override
	public T get() {
		return this.value;
	}

	@Override
	public String type() {
		return this.type.toString();
	}

	@Override
	public String toString() {
		return "[" + this.get() + "]<" + this.type() + ">";
	}
}
