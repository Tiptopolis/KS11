package com.uchump.prime.Metatron.Lib.exp4M.tokenizer;

import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.A_I.iToken;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aList;

public class _Token<T> extends _Map.Entry<Integer,T> implements iToken<T>{

	public static final aList<String> TYPES = new aList<String>();

	static {
		TYPES.add("NUMBER");
		TYPES.add("OPERATOR");
		TYPES.add("FUNCTION");
		TYPES.add("PARENTHESES_OPEN");
		TYPES.add("PARENTHESES_CLOSE");
		TYPES.add("VARIABLE");
		TYPES.add("SEPARATOR");
		TYPES.add("WORD");
	}


	
	public static int getIndexOf(String s)
	{
		return TYPES.indexOf(s);
	}
	////////////////////////////////
	protected T get;
	private final int type;
	public _Token(int type) {
		this.get = (T) _Token.class;
		this.type = type;
	}

	public T get() {
		return this.get;
	}

	public int getType() {
		return type;
	}

	public String type() {
		return TYPES.get(this.type);
	}
}
