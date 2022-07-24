package com.uchump.prime.Core.Primitive;

import com.uchump.prime.Core.Primitive.Struct.aList;

public class _Nodes {
	public static final aList<String> TYPES = new aList<String>();

	static {
		TYPES.appendAll("ANY", "ELEMENT", "ATTRIBUTE", "TEXT", "ENTITY", "COMMAND", "TYPE", "TYPE", "NaN");
	}
}
